package com.example.catatankeuanganpribadi.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.domain.model.TransactionDetails
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.components.AnimatedBudgetBar
import com.example.catatankeuanganpribadi.presentation.components.DonutChart
import com.example.catatankeuanganpribadi.presentation.components.DonutLegend
import com.example.catatankeuanganpribadi.presentation.components.DonutSlice
import com.example.catatankeuanganpribadi.presentation.components.EmptyState
import com.example.catatankeuanganpribadi.presentation.components.PeriodFilterRow
import com.example.catatankeuanganpribadi.presentation.components.SectionHeader
import com.example.catatankeuanganpribadi.presentation.components.chartColor
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import com.example.catatankeuanganpribadi.presentation.util.Formatters
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel? = null
) {
    if (LocalInspectionMode.current && viewModel == null) {
        StatisticsContent(
            uiState = StatisticsUiState(),
            onUpdatePeriod = {},
            modifier = modifier
        )
    } else {
        val actualViewModel: StatisticsViewModel =
            viewModel ?: viewModel(factory = FinanceViewModelFactory.statistics())
        val uiState by actualViewModel.uiState.collectAsStateWithLifecycle()

        StatisticsContent(
            uiState = uiState,
            onUpdatePeriod = actualViewModel::updatePeriod,
            modifier = modifier
        )
    }
}

@Composable
private fun StatisticsContent(
    uiState: StatisticsUiState,
    onUpdatePeriod: (PeriodFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "header") {
            Text(
                "Statistik",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item(key = "period") {
            PeriodFilterRow(
                selected = uiState.selectedPeriod,
                onSelected = onUpdatePeriod
            )
        }

        uiState.topInsight?.let { insight ->
            item(key = "insight") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                ) {
                    Text(
                        text = "Insight: $insight",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        statisticsSectionItems(
            title = "Pemasukan",
            transactions = uiState.incomeTransactions,
            emptySubtitle = "Tambahkan pemasukan untuk melihat distribusi kategori"
        )

        statisticsSectionItems(
            title = "Pengeluaran",
            transactions = uiState.expenseTransactions,
            emptySubtitle = "Tambahkan pengeluaran untuk melihat distribusi kategori"
        )

        item { Spacer(Modifier.height(8.dp)) }
    }
}

private fun LazyListScope.statisticsSectionItems(
    title: String,
    transactions: List<TransactionDetails>,
    emptySubtitle: String
) {
    val totalsByCategory = transactions
        .groupBy { it.categoryName ?: "Tanpa Kategori" }
        .mapValues { (_, items) -> items.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val totalAmount = totalsByCategory.sumOf { it.second }.coerceAtLeast(1L)

    val donutSlices = totalsByCategory.mapIndexed { index, (name, amount) ->
        DonutSlice(
            label = name,
            value = amount.toFloat(),
            color = chartColor(index)
        )
    }

    item(key = "${title}_header") {
        SectionHeader(title)
    }

    if (donutSlices.isEmpty()) {
        item(key = "${title}_empty") {
            EmptyState(
                title = "Belum ada data $title",
                subtitle = emptySubtitle
            )
        }
        return
    }

    item(key = "${title}_chart") {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DonutChart(
                    slices = donutSlices,
                    chartSize = 180.dp,
                    strokeWidth = 28.dp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Total $title",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    Formatters.rupiah(totalAmount),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                DonutLegend(
                    slices = donutSlices,
                    total = totalAmount
                )
            }
        }
    }

    item(key = "${title}_breakdown_header") {
        SectionHeader("Detail per Kategori $title")
    }

    itemsIndexed(
        items = totalsByCategory,
        key = { _, pair -> "$title-${pair.first}" }
    ) { index, (category, amount) ->
        val percentage = (amount.toDouble() / totalAmount.toDouble() * 100).roundToInt()
        val color = chartColor(index)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        category,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "$percentage%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                Text(
                    Formatters.rupiah(amount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AnimatedBudgetBar(
                    progress = percentage / 100f,
                    color = color,
                    height = 8.dp
                )
            }
        }
    }
}
