package com.example.catatankeuanganpribadi.presentation.statistics

import com.example.catatankeuanganpribadi.domain.model.TransactionDetails
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val selectedPeriod: PeriodFilter = PeriodFilter.MONTH,
    val incomeTransactions: List<TransactionDetails> = emptyList(),
    val expenseTransactions: List<TransactionDetails> = emptyList(),
    val topInsight: String? = null
)
