package com.example.catatankeuanganpribadi.presentation.transactionlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.domain.model.TransactionSortOption
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.components.EmptyState
import com.example.catatankeuanganpribadi.presentation.components.PeriodFilterRow
import com.example.catatankeuanganpribadi.presentation.components.TransactionRow
import com.example.catatankeuanganpribadi.presentation.util.Formatters

@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionListViewModel = viewModel(factory = FinanceViewModelFactory.transactionList())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val groupedTransactions = uiState.transactions.groupBy { Formatters.dayHeader(it.dateTime) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Header ──
        item(key = "header") {
            Text(
                "Transaksi",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ── Period Filter ──
        item(key = "period") {
            PeriodFilterRow(
                selected = uiState.selectedPeriod,
                onSelected = viewModel::updatePeriod
            )
        }

        // ── Search Bar ──
        item(key = "search") {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari catatan, kategori, akun...") },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        // ── Account Chips ──
        item(key = "accounts") {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.accounts, key = { it.id }) { account ->
                    FilterChip(
                        selected = uiState.selectedAccountId == account.id,
                        onClick = {
                            viewModel.updateAccount(
                                if (uiState.selectedAccountId == account.id) null else account.id
                            )
                        },
                        label = { Text(account.name, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // ── Sort & Category Chips ──
        item(key = "sort_category") {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = uiState.sortOption == TransactionSortOption.LATEST,
                        onClick = { viewModel.updateSortOption(TransactionSortOption.LATEST) },
                        label = { Text("Terbaru", style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.sortOption == TransactionSortOption.AMOUNT_DESC,
                        onClick = { viewModel.updateSortOption(TransactionSortOption.AMOUNT_DESC) },
                        label = { Text("Nominal ↓", style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(uiState.categories, key = { it.id }) { category ->
                    FilterChip(
                        selected = uiState.selectedCategoryId == category.id,
                        onClick = {
                            viewModel.updateCategory(
                                if (uiState.selectedCategoryId == category.id) null else category.id
                            )
                        },
                        label = { Text(category.name, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // ── Grouped Transaction List ──
        if (groupedTransactions.isEmpty()) {
            item(key = "empty") {
                EmptyState(
                    title = "Tidak ada transaksi",
                    subtitle = "Coba ubah filter periode, akun, atau kata kunci"
                )
            }
        } else {
            groupedTransactions.forEach { (date, transactions) ->
                item(key = "date_$date") {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(transactions, key = { it.id }) { transaction ->
                    TransactionRow(
                        transaction = transaction,
                        onDelete = { viewModel.deleteTransaction(transaction.id) }
                    )
                    if (transaction != transactions.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 58.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}
