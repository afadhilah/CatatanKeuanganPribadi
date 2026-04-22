package com.example.catatankeuanganpribadi.presentation.transactionlist

import com.example.catatankeuanganpribadi.domain.model.Account
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.TransactionDetails
import com.example.catatankeuanganpribadi.domain.model.TransactionSortOption
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter

data class TransactionListUiState(
    val isLoading: Boolean = true,
    val selectedPeriod: PeriodFilter = PeriodFilter.MONTH,
    val selectedAccountId: Long? = null,
    val selectedCategoryId: Long? = null,
    val searchQuery: String = "",
    val sortOption: TransactionSortOption = TransactionSortOption.LATEST,
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val transactions: List<TransactionDetails> = emptyList()
)
