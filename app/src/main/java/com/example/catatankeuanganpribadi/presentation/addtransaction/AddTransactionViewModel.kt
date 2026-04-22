package com.example.catatankeuanganpribadi.presentation.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.Account
import com.example.catatankeuanganpribadi.domain.model.AccountType
import com.example.catatankeuanganpribadi.domain.model.SaveTransactionRequest
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.domain.repository.AccountRepository
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import com.example.catatankeuanganpribadi.domain.usecase.SaveTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.seedDefaultCategoriesIfNeeded()
            seedDefaultAccountIfNeeded()
            
            // Start observing data after seeding
            launch { observeAccounts() }
            launch { observeCategories() }
        }
    }

    private suspend fun observeAccounts() {
        accountRepository.observeAccounts().collect { accounts ->
            _uiState.update { state ->
                state.copy(
                    accounts = accounts,
                    selectedAccountId = state.selectedAccountId ?: accounts.firstOrNull()?.id,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun observeCategories() {
        // Observe selectedType changes and switch category observation
        _uiState.map { it.selectedType }
            .distinctUntilChanged()
            .flatMapLatest { type ->
                categoryRepository.observeCategoriesByType(type)
            }
            .collect { categories ->
                _uiState.update { state ->
                    state.copy(
                        categories = categories,
                        selectedCategoryId = if (state.selectedType == TransactionType.TRANSFER) {
                            null
                        } else {
                            state.selectedCategoryId ?: categories.firstOrNull()?.id
                        }
                    )
                }
            }
    }

    private suspend fun seedDefaultAccountIfNeeded() {
        if (accountRepository.observeAccounts().first().isNotEmpty()) return

        accountRepository.saveAccount(
            Account(
                id = 0L,
                name = "Cash",
                type = AccountType.CASH,
                balance = 0L,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    fun updateType(type: TransactionType) {
        _uiState.update {
            it.copy(
                selectedType = type,
                selectedCategoryId = if (type == TransactionType.TRANSFER) null else it.selectedCategoryId,
                selectedTransferAccountId = if (type == TransactionType.TRANSFER) it.selectedTransferAccountId else null,
                errorMessage = null,
                saveCompleted = false
            )
        }
    }

    fun updateAmount(value: String) {
        val filtered = value.filter(Char::isDigit)
        _uiState.update { it.copy(amountInput = filtered, errorMessage = null) }
    }

    fun updateAccount(accountId: Long) {
        _uiState.update { it.copy(selectedAccountId = accountId, errorMessage = null) }
    }

    fun updateTransferAccount(accountId: Long?) {
        _uiState.update { it.copy(selectedTransferAccountId = accountId, errorMessage = null) }
    }

    fun updateCategory(categoryId: Long?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId, errorMessage = null) }
    }

    fun updateNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val currentState = uiState.value
            val amount = currentState.amountInput.toLongOrNull()

            if (amount == null || amount <= 0L) {
                _uiState.update { it.copy(errorMessage = "Nominal harus lebih besar dari 0") }
                return@launch
            }

            val accountId = currentState.selectedAccountId
            if (accountId == null) {
                _uiState.update { it.copy(errorMessage = "Pilih akun terlebih dahulu") }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true, errorMessage = null, saveCompleted = false) }

            runCatching {
                saveTransactionUseCase(
                    SaveTransactionRequest(
                        type = currentState.selectedType,
                        amount = amount,
                        accountId = accountId,
                        transferAccountId = currentState.selectedTransferAccountId,
                        categoryId = currentState.selectedCategoryId,
                        dateTime = currentState.dateTime,
                        note = currentState.note.ifBlank { null }
                    )
                )
            }.onSuccess {
                _uiState.update {
                    AddTransactionUiState(
                        isLoading = false,
                        selectedType = it.selectedType,
                        accounts = it.accounts,
                        categories = it.categories,
                        selectedAccountId = it.selectedAccountId,
                        selectedTransferAccountId = null,
                        selectedCategoryId = if (it.selectedType == TransactionType.TRANSFER) null else it.categories.firstOrNull()?.id,
                        dateTime = System.currentTimeMillis(),
                        saveCompleted = true
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Gagal menyimpan transaksi"
                    )
                }
            }
        }
    }

    fun consumeSaveResult() {
        _uiState.update { it.copy(saveCompleted = false) }
    }
}
