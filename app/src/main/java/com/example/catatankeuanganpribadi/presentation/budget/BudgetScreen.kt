package com.example.catatankeuanganpribadi.presentation.budget

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.domain.model.BudgetStatus
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.components.AnimatedBudgetBar
import com.example.catatankeuanganpribadi.presentation.components.EmptyState
import com.example.catatankeuanganpribadi.presentation.components.SectionHeader
import com.example.catatankeuanganpribadi.presentation.util.Formatters
import com.example.catatankeuanganpribadi.ui.theme.AmberYellow
import com.example.catatankeuanganpribadi.ui.theme.CoralRed
import com.example.catatankeuanganpribadi.ui.theme.MintGreen

@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel? = null
) {
    if (LocalInspectionMode.current && viewModel == null) {
        BudgetContent(
            uiState = BudgetUiState(),
            onUpdateCategory = {},
            onUpdateLimitAmount = {},
            onSaveBudget = {},
            modifier = modifier
        )
    } else {
        val actualViewModel: BudgetViewModel = viewModel ?: viewModel(factory = FinanceViewModelFactory.budget())
        val uiState by actualViewModel.uiState.collectAsStateWithLifecycle()

        BudgetContent(
            uiState = uiState,
            onUpdateCategory = actualViewModel::updateCategory,
            onUpdateLimitAmount = actualViewModel::updateLimitAmount,
            onSaveBudget = actualViewModel::saveBudget,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetContent(
    uiState: BudgetUiState,
    onUpdateCategory: (Long) -> Unit,
    onUpdateLimitAmount: (String) -> Unit,
    onSaveBudget: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header ──
        item(key = "header") {
            Text(
                "Budget",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ── Set Budget Card ──
        item(key = "form") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Atur Budget",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Category dropdown
                    BudgetCategoryDropdown(
                        selectedLabel = uiState.categories
                            .firstOrNull { it.id == uiState.selectedCategoryId }?.name.orEmpty(),
                        options = uiState.categories.map { it.id to it.name },
                        onSelected = onUpdateCategory
                    )

                    // Limit input
                    OutlinedTextField(
                        value = uiState.limitAmountInput,
                        onValueChange = onUpdateLimitAmount,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Limit (Rupiah)") },
                        placeholder = { Text("Contoh: 500000") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Error
                    if (!uiState.errorMessage.isNullOrBlank()) {
                        Text(
                            text = uiState.errorMessage.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Save button
                    Button(
                        onClick = onSaveBudget,
                        enabled = !uiState.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            if (uiState.isSaving) "Menyimpan..." else "Simpan Budget",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // ── Budget List Header ──
        item(key = "list_header") {
            SectionHeader("Budget Aktif")
        }

        // ── Budget Items ──
        if (uiState.budgetUsages.isEmpty()) {
            item(key = "empty") {
                EmptyState(
                    title = "Belum ada budget",
                    subtitle = "Atur budget per kategori untuk memantau pengeluaranmu"
                )
            }
        } else {
            items(uiState.budgetUsages, key = { it.budgetId }) { usage ->
                val (statusColor, statusText) = when (usage.status) {
                    BudgetStatus.SAFE -> MintGreen to "Aman"
                    BudgetStatus.WARNING -> AmberYellow to "Hampir penuh"
                    BudgetStatus.EXCEEDED -> CoralRed to "Melebihi limit!"
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                usage.categoryName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(statusColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    statusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = statusColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Amount text
                        Row {
                            Text(
                                Formatters.rupiah(usage.usedAmount),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = statusColor
                            )
                            Text(
                                " / ${Formatters.rupiah(usage.limitAmount)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Animated progress bar
                        AnimatedBudgetBar(
                            progress = usage.progress,
                            color = statusColor
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

// ─── Budget Category Dropdown ────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetCategoryDropdown(
    selectedLabel: String,
    options: List<Pair<Long, String>>,
    onSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Kategori") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (id, label) ->
                DropdownMenuItem(
                    text = { Text(label, style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        expanded = false
                        onSelected(id)
                    }
                )
            }
        }
    }
}
