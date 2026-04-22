package com.example.catatankeuanganpribadi.presentation.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.components.EmptyState
import com.example.catatankeuanganpribadi.presentation.components.GradientHeroCard
import com.example.catatankeuanganpribadi.presentation.components.PeriodFilterRow
import com.example.catatankeuanganpribadi.presentation.components.SectionHeader
import com.example.catatankeuanganpribadi.presentation.components.ShortcutChip
import com.example.catatankeuanganpribadi.presentation.components.TransactionRow

@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onOpenBudget: () -> Unit,
    onOpenStatistics: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = FinanceViewModelFactory.dashboard())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Greeting ──
        item(key = "greeting") {
            Column {
                Text(
                    "Halo! 👋",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Pantau keuanganmu hari ini",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ── Period Filter ──
        item(key = "period") {
            PeriodFilterRow(
                selected = uiState.selectedPeriod,
                onSelected = viewModel::updatePeriod
            )
        }

        // ── Hero Balance Card ──
        item(key = "hero") {
            AnimatedContent(
                targetState = uiState.summary,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "heroAnimation"
            ) { summary ->
                GradientHeroCard(
                    totalBalance = summary.totalBalance,
                    totalIncome = summary.totalIncome,
                    totalExpense = summary.totalExpense
                )
            }
        }

        // ── Quick Actions ──
        item(key = "quick_actions") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShortcutChip(label = "＋ Tambah", onClick = onAddTransaction)
                ShortcutChip(label = "💰 Budget", onClick = onOpenBudget)
                ShortcutChip(label = "📊 Statistik", onClick = onOpenStatistics)
            }
        }

        // ── Recent Transactions Header ──
        item(key = "recent_header") {
            SectionHeader("Transaksi Terbaru")
        }

        // ── Transactions ──
        if (uiState.recentTransactions.isEmpty()) {
            item(key = "empty") {
                EmptyState(
                    title = "Belum ada transaksi",
                    subtitle = "Tambahkan pemasukan atau pengeluaran pertamamu"
                )
            }
        } else {
            items(
                items = uiState.recentTransactions,
                key = { it.id }
            ) { transaction ->
                TransactionRow(transaction = transaction)
                if (transaction != uiState.recentTransactions.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 58.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}
