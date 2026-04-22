package com.example.catatankeuanganpribadi.presentation.transactionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.TransactionFilter
import com.example.catatankeuanganpribadi.domain.model.TransactionSortOption
import com.example.catatankeuanganpribadi.domain.repository.AccountRepository
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import com.example.catatankeuanganpribadi.domain.usecase.DeleteTransactionUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveFilteredTransactionsUseCase
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import com.example.catatankeuanganpribadi.presentation.util.DateRangeFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val observeFilteredTransactionsUseCase: ObserveFilteredTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val filters = MutableStateFlow(TransactionListUiState())

    val uiState: StateFlow<TransactionListUiState> = filters
        .flatMapLatest { state ->
            combine(
                accountRepository.observeAccounts(),
                categoryRepository.observeCategories(),
                observeFilteredTransactionsUseCase(
                    TransactionFilter(
                        dateRange = DateRangeFactory.create(state.selectedPeriod),
                        accountId = state.selectedAccountId,
                        categoryId = state.selectedCategoryId,
                        searchQuery = state.searchQuery,
                        sortOption = state.sortOption
                    )
                )
            ) { accounts, categories, transactions ->
                state.copy(
                    isLoading = false,
                    accounts = accounts,
                    categories = categories,
                    transactions = transactions
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionListUiState()
        )

    fun updatePeriod(periodFilter: PeriodFilter) {
        filters.update { it.copy(selectedPeriod = periodFilter) }
    }

    fun updateAccount(accountId: Long?) {
        filters.update { it.copy(selectedAccountId = accountId) }
    }

    fun updateCategory(categoryId: Long?) {
        filters.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun updateSearchQuery(query: String) {
        filters.update { it.copy(searchQuery = query) }
    }

    fun updateSortOption(sortOption: TransactionSortOption) {
        filters.update { it.copy(sortOption = sortOption) }
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            runCatching { deleteTransactionUseCase(transactionId) }
        }
    }
}
