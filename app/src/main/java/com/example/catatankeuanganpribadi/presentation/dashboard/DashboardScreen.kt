package com.example.catatankeuanganpribadi.presentation.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.components.*
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter

@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onOpenBudget: () -> Unit,
    onOpenStatistics: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel? = null
) {
    if (LocalInspectionMode.current && viewModel == null) {
        DashboardContent(
            uiState = DashboardUiState(isLoading = false),
            onUpdatePeriod = {},
            onAddTransaction = onAddTransaction,
            onOpenBudget = onOpenBudget,
            onOpenStatistics = onOpenStatistics,
            onOpenSettings = onOpenSettings,
            modifier = modifier
        )
    } else {
        val actualViewModel: DashboardViewModel = viewModel ?: viewModel(factory = FinanceViewModelFactory.dashboard())
        val uiState by actualViewModel.uiState.collectAsStateWithLifecycle()

        DashboardContent(
            uiState = uiState,
            onUpdatePeriod = actualViewModel::updatePeriod,
            onAddTransaction = onAddTransaction,
            onOpenBudget = onOpenBudget,
            onOpenStatistics = onOpenStatistics,
            onOpenSettings = onOpenSettings,
            modifier = modifier
        )
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onUpdatePeriod: (PeriodFilter) -> Unit,
    onAddTransaction: () -> Unit,
    onOpenBudget: () -> Unit,
    onOpenStatistics: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Greeting ──
        item(key = "greeting") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
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
                
                IconButton(onClick = onOpenSettings) {
                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = "Pengaturan",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ── Period Filter ──
        item(key = "period") {
            PeriodFilterRow(
                selected = uiState.selectedPeriod,
                onSelected = onUpdatePeriod
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
