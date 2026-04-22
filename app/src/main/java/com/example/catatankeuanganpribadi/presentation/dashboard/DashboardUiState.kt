package com.example.catatankeuanganpribadi.presentation.dashboard

import com.example.catatankeuanganpribadi.domain.model.DashboardSummary
import com.example.catatankeuanganpribadi.domain.model.TransactionDetails
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter

data class DashboardUiState(
    val isLoading: Boolean = true,
    val selectedPeriod: PeriodFilter = PeriodFilter.MONTH,
    val summary: DashboardSummary = DashboardSummary(
        totalBalance = 0L,
        totalIncome = 0L,
        totalExpense = 0L
    ),
    val recentTransactions: List<TransactionDetails> = emptyList()
)
