package com.example.catatankeuanganpribadi.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.usecase.ObserveDashboardSummaryUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveRecentTransactionsUseCase
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import com.example.catatankeuanganpribadi.presentation.util.DateRangeFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
    private val observeRecentTransactionsUseCase: ObserveRecentTransactionsUseCase
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(PeriodFilter.MONTH)

    val uiState: StateFlow<DashboardUiState> = selectedPeriod
        .flatMapLatest { period ->
            val dateRange = DateRangeFactory.create(period)
            combine(
                observeDashboardSummaryUseCase(dateRange),
                observeRecentTransactionsUseCase(5)
            ) { summary, recentTransactions ->
                DashboardUiState(
                    isLoading = false,
                    selectedPeriod = period,
                    summary = summary,
                    recentTransactions = recentTransactions
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )

    fun updatePeriod(periodFilter: PeriodFilter) {
        selectedPeriod.value = periodFilter
    }
}
