package com.example.catatankeuanganpribadi.domain.model

data class TransactionFilter(
    val dateRange: DateRange,
    val accountId: Long? = null,
    val categoryId: Long? = null,
    val searchQuery: String? = null,
    val sortOption: TransactionSortOption = TransactionSortOption.LATEST
)
