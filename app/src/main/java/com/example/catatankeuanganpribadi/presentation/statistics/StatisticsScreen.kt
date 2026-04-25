package com.example.catatankeuanganpribadi.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.catatankeuanganpribadi.presentation.components.chartColor
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import com.example.catatankeuanganpribadi.presentation.util.Formatters
import kotlin.math.roundToInt

private enum class StatisticsMode(val label: String) {
    INCOME("Pemasukan"),
    EXPENSE("Pengeluaran")
}

private data class AggregatedStatistics(
    val totalAmount: Long,
    val averageAmount: Long,
    val transactionCount: Int,
    val topCategory: String?,
    val topCategoryPercent: Int,
    val totalsByCategory: List<Pair<String, Long>>
)

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
    var mode by rememberSaveable { mutableStateOf(StatisticsMode.EXPENSE) }

    val selectedTransactions = if (mode == StatisticsMode.INCOME) {
        uiState.incomeTransactions
    } else {
        uiState.expenseTransactions
    }
    val aggregate = buildAggregate(selectedTransactions)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(key = "header") {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Statistik",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Lihat pola pemasukan dan pengeluaranmu.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(key = "controls") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PeriodFilterRow(
                        selected = uiState.selectedPeriod,
                        onSelected = onUpdatePeriod
                    )

                    StatisticsModeSelector(
                        selected = mode,
                        onSelected = { mode = it }
                    )
                }
            }
        }

        item(key = "summary") {
            StatisticsSummaryCard(
                mode = mode,
                aggregate = aggregate
            )
        }

        statisticsDetailsItems(
            mode = mode,
            aggregate = aggregate,
            emptySubtitle = if (mode == StatisticsMode.INCOME) {
                "Tambahkan pemasukan untuk melihat distribusi kategori"
            } else {
                "Tambahkan pengeluaran untuk melihat distribusi kategori"
            }
        )

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun StatisticsModeSelector(
    selected: StatisticsMode,
    onSelected: (StatisticsMode) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            StatisticsMode.values().forEach { mode ->
                val isSelected = mode == selected
                Surface(
                    onClick = { onSelected(mode) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(9.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    tonalElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsSummaryCard(
    mode: StatisticsMode,
    aggregate: AggregatedStatistics
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Ringkasan ${mode.label}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        Formatters.rupiah(aggregate.totalAmount),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryStatCard(
                    label = "Rata-rata",
                    value = Formatters.rupiah(aggregate.averageAmount),
                    modifier = Modifier.weight(1f)
                )
                SummaryStatCard(
                    label = "Transaksi",
                    value = aggregate.transactionCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            val topCategoryText = aggregate.topCategory ?: "Belum ada data"
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = if (aggregate.topCategory != null) {
                        "Kategori terbesar: $topCategoryText (${aggregate.topCategoryPercent}%)"
                    } else {
                        "Kategori terbesar: $topCategoryText"
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SummaryStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun LazyListScope.statisticsDetailsItems(
    mode: StatisticsMode,
    aggregate: AggregatedStatistics,
    emptySubtitle: String
) {
    val donutSlices = aggregate.totalsByCategory.mapIndexed { index, (name, amount) ->
        DonutSlice(
            label = name,
            value = amount.toFloat(),
            color = chartColor(index)
        )
    }

    if (donutSlices.isEmpty()) {
        item(key = "${mode.name}_empty") {
            EmptyState(
                title = "Belum ada data ${mode.label}",
                subtitle = emptySubtitle
            )
        }
        return
    }

    item(key = "${mode.name}_chart") {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Distribusi ${mode.label}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(14.dp))

                DonutChart(
                    slices = donutSlices,
                    chartSize = 168.dp,
                    strokeWidth = 24.dp
                )

                Spacer(Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Total ${Formatters.rupiah(aggregate.totalAmount)}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(16.dp))

                DonutLegend(
                    slices = donutSlices,
                    total = aggregate.totalAmount.coerceAtLeast(1L)
                )
            }
        }
    }

    item(key = "${mode.name}_breakdown_header") {
        Text(
            text = "Detail Kategori",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    itemsIndexed(
        items = aggregate.totalsByCategory,
        key = { _, pair -> "${mode.name}-${pair.first}" }
    ) { index, (category, amount) ->
        val total = aggregate.totalAmount.coerceAtLeast(1L)
        val percentage = (amount.toDouble() / total.toDouble() * 100).roundToInt()
        val color = chartColor(index)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .padding(1.dp)
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = CircleShape,
                                color = color
                            ) {}
                        }
                        Text(
                            category,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = color.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = "$percentage%",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Formatters.rupiah(amount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "${aggregate.transactionCount} trx",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                AnimatedBudgetBar(
                    progress = percentage / 100f,
                    color = color,
                    height = 6.dp
                )
            }
        }
    }
}

private fun buildAggregate(transactions: List<TransactionDetails>): AggregatedStatistics {
    val totalsByCategory = transactions
        .groupBy { it.categoryName ?: "Tanpa Kategori" }
        .mapValues { (_, items) -> items.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val totalAmount = totalsByCategory.sumOf { it.second }
    val averageAmount = if (transactions.isEmpty()) 0L else totalAmount / transactions.size
    val topCategory = totalsByCategory.firstOrNull()
    val topPercent = if (topCategory == null || totalAmount <= 0L) {
        0
    } else {
        (topCategory.second.toDouble() / totalAmount.toDouble() * 100).roundToInt()
    }

    return AggregatedStatistics(
        totalAmount = totalAmount,
        averageAmount = averageAmount,
        transactionCount = transactions.size,
        topCategory = topCategory?.first,
        topCategoryPercent = topPercent,
        totalsByCategory = totalsByCategory
    )
}
