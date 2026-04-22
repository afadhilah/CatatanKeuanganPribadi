package com.example.catatankeuanganpribadi.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.domain.usecase.ObserveTransactionsUseCase
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import com.example.catatankeuanganpribadi.presentation.util.DateRangeFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.math.roundToInt

class StatisticsViewModel(
    private val observeTransactionsUseCase: ObserveTransactionsUseCase
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(PeriodFilter.MONTH)

    val uiState: StateFlow<StatisticsUiState> = selectedPeriod
        .flatMapLatest { period ->
            observeTransactionsUseCase(DateRangeFactory.create(period))
                .map { transactions ->
                    val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
                    val insight = buildTopInsight(expenseTransactions)
                    StatisticsUiState(
                        isLoading = false,
                        selectedPeriod = period,
                        expenseTransactions = expenseTransactions,
                        topInsight = insight
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatisticsUiState()
        )

    fun updatePeriod(periodFilter: PeriodFilter) {
        selectedPeriod.value = periodFilter
    }

    private fun buildTopInsight(expenseTransactions: List<com.example.catatankeuanganpribadi.domain.model.TransactionDetails>): String? {
        val totalExpense = expenseTransactions.sumOf { it.amount }
        if (totalExpense <= 0L) return null

        val topCategory = expenseTransactions
            .groupBy { it.categoryName ?: "Tanpa Kategori" }
            .mapValues { (_, items) -> items.sumOf { it.amount } }
            .maxByOrNull { it.value } ?: return null

        val percentage = (topCategory.value.toDouble() / totalExpense.toDouble() * 100.0).roundToInt()
        return "Kategori terbesar periode ini: ${topCategory.key} ($percentage%)"
    }
}
