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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.components.AnimatedBudgetBar
import com.example.catatankeuanganpribadi.presentation.components.DonutChart
import com.example.catatankeuanganpribadi.presentation.components.DonutLegend
import com.example.catatankeuanganpribadi.presentation.components.DonutSlice
import com.example.catatankeuanganpribadi.presentation.components.EmptyState
import com.example.catatankeuanganpribadi.presentation.components.PeriodFilterRow
import com.example.catatankeuanganpribadi.presentation.components.SectionHeader
import com.example.catatankeuanganpribadi.presentation.components.chartColor
import com.example.catatankeuanganpribadi.presentation.util.Formatters
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = viewModel(factory = FinanceViewModelFactory.statistics())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val totalsByCategory = uiState.expenseTransactions
        .groupBy { it.categoryName ?: "Tanpa Kategori" }
        .mapValues { (_, items) -> items.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val totalExpense = totalsByCategory.sumOf { it.second }.coerceAtLeast(1L)

    val donutSlices = totalsByCategory.mapIndexed { index, (name, amount) ->
        DonutSlice(
            label = name,
            value = amount.toFloat(),
            color = chartColor(index)
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header ──
        item(key = "header") {
            Text(
                "Statistik",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ── Period ──
        item(key = "period") {
            PeriodFilterRow(
                selected = uiState.selectedPeriod,
                onSelected = viewModel::updatePeriod
            )
        }

        // ── Insight Card ──
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
                        text = "💡 $insight",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // ── Donut Chart + Legend ──
        if (donutSlices.isEmpty()) {
            item(key = "empty") {
                EmptyState(
                    title = "Belum ada data statistik",
                    subtitle = "Tambahkan pengeluaran untuk melihat distribusi kategori"
                )
            }
        } else {
            item(key = "chart") {
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
                        // Donut Chart
                        DonutChart(
                            slices = donutSlices,
                            chartSize = 180.dp,
                            strokeWidth = 28.dp
                        )

                        Spacer(Modifier.height(8.dp))

                        // Total text in/below chart
                        Text(
                            "Total Pengeluaran",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            Formatters.rupiah(totalExpense),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(20.dp))

                        // Legend
                        DonutLegend(
                            slices = donutSlices,
                            total = totalExpense
                        )
                    }
                }
            }

            // ── Category Breakdown Bars ──
            item(key = "breakdown_header") {
                SectionHeader("Detail per Kategori")
            }

            itemsIndexed(totalsByCategory, key = { _, pair -> pair.first }) { index, (category, amount) ->
                val percentage = (amount.toDouble() / totalExpense.toDouble() * 100).roundToInt()
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

        item { Spacer(Modifier.height(8.dp)) }
    }
}
