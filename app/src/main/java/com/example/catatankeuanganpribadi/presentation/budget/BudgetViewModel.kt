package com.example.catatankeuanganpribadi.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.Budget
import com.example.catatankeuanganpribadi.domain.model.BudgetPeriod
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.domain.repository.BudgetRepository
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import com.example.catatankeuanganpribadi.domain.usecase.ObserveBudgetUsageUseCase
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import com.example.catatankeuanganpribadi.presentation.util.DateRangeFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetViewModel(
    private val observeBudgetUsageUseCase: ObserveBudgetUsageUseCase,
    private val categoryRepository: CategoryRepository,
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState(selectedPeriod = PeriodFilter.MONTH))
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch { observeCategories() }
            launch { observeBudgetUsages() }
        }
    }

    private suspend fun observeCategories() {
        categoryRepository.observeCategoriesByType(TransactionType.EXPENSE).collect { expenseCategories ->
            _uiState.update { state ->
                val selectedCategoryId = state.selectedCategoryId ?: expenseCategories.firstOrNull()?.id
                state.copy(
                    categories = expenseCategories,
                    selectedCategoryId = selectedCategoryId,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun observeBudgetUsages() {
        _uiState.map { it.selectedPeriod }
            .distinctUntilChanged()
            .flatMapLatest { period ->
                observeBudgetUsageUseCase(DateRangeFactory.create(period))
            }
            .collect { usages ->
                _uiState.update { state ->
                    state.copy(budgetUsages = usages)
                }
            }
    }

    fun updatePeriod(periodFilter: PeriodFilter) {
        _uiState.update { it.copy(selectedPeriod = periodFilter) }
    }

    fun updateCategory(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId, errorMessage = null) }
        viewModelScope.launch {
            val existingBudget = budgetRepository.getBudgetByCategory(categoryId)
            _uiState.update {
                it.copy(
                    limitAmountInput = existingBudget?.limitAmount?.toString().orEmpty()
                )
            }
        }
    }

    fun updateLimitAmount(value: String) {
        val filtered = value.filter(Char::isDigit)
        _uiState.update {
            it.copy(
                limitAmountInput = filtered,
                errorMessage = null
            )
        }
    }

    fun saveBudget() {
        viewModelScope.launch {
            val currentState = uiState.value
            val categoryId = currentState.selectedCategoryId
            if (categoryId == null) {
                _uiState.update { it.copy(errorMessage = "Pilih kategori pengeluaran") }
                return@launch
            }

            val limitAmount = currentState.limitAmountInput.toLongOrNull()
            if (limitAmount == null || limitAmount <= 0L) {
                _uiState.update { it.copy(errorMessage = "Limit budget harus lebih besar dari 0") }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            runCatching {
                val existing = budgetRepository.getBudgetByCategory(categoryId)
                budgetRepository.saveBudget(
                    Budget(
                        id = existing?.id ?: 0L,
                        categoryId = categoryId,
                        limitAmount = limitAmount,
                        period = BudgetPeriod.MONTHLY
                    )
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        limitAmountInput = "",
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Gagal menyimpan budget"
                    )
                }
            }
        }
    }
}
