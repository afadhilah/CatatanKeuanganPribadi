package com.example.catatankeuanganpribadi.presentation.addtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory
import com.example.catatankeuanganpribadi.presentation.util.Formatters
import com.example.catatankeuanganpribadi.ui.theme.CoralRed
import com.example.catatankeuanganpribadi.ui.theme.MintGreen
import com.example.catatankeuanganpribadi.ui.theme.TealBlue

@Composable
fun AddTransactionScreen(
    onBackAfterSave: () -> Unit,
    modifier: Modifier = Modifier,
    transactionId: Long = -1L,
    viewModel: AddTransactionViewModel = viewModel(
        key = if (transactionId != -1L) "edit_$transactionId" else "add",
        factory = FinanceViewModelFactory.addTransaction(transactionId)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddTransactionContent(
        uiState = uiState,
        onUpdateType = viewModel::updateType,
        onUpdateAmount = viewModel::updateAmount,
        onUpdateAccount = viewModel::updateAccount,
        onUpdateTransferAccount = viewModel::updateTransferAccount,
        onUpdateCategory = viewModel::updateCategory,
        onUpdateDateTime = viewModel::updateDateTime,
        onUpdateNote = viewModel::updateNote,
        onSaveTransaction = viewModel::saveTransaction,
        onConsumeSaveResult = viewModel::consumeSaveResult,
        onBackAfterSave = onBackAfterSave,
        modifier = modifier,
        isEditMode = transactionId != -1L
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionContent(
    uiState: AddTransactionUiState,
    onUpdateType: (TransactionType) -> Unit,
    onUpdateAmount: (String) -> Unit,
    onUpdateAccount: (Long) -> Unit,
    onUpdateTransferAccount: (Long) -> Unit,
    onUpdateCategory: (Long) -> Unit,
    onUpdateDateTime: (Long) -> Unit,
    onUpdateNote: (String) -> Unit,
    onSaveTransaction: () -> Unit,
    onConsumeSaveResult: () -> Unit,
    onBackAfterSave: () -> Unit,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false
) {
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.saveCompleted) {
        if (uiState.saveCompleted) {
            onConsumeSaveResult()
            onBackAfterSave()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.dateTime
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onUpdateDateTime(it) }
                    showDatePicker = false
                }) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                if (isEditMode) "Edit Transaksi" else "Tambah Transaksi",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ── Type Selector ──
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransactionTypeChip(
                    label = "Masuk",
                    icon = Icons.Rounded.ArrowDownward,
                    selected = uiState.selectedType == TransactionType.INCOME,
                    color = MintGreen,
                    onClick = { onUpdateType(TransactionType.INCOME) },
                    modifier = Modifier.weight(1f)
                )
                TransactionTypeChip(
                    label = "Keluar",
                    icon = Icons.Rounded.ArrowUpward,
                    selected = uiState.selectedType == TransactionType.EXPENSE,
                    color = CoralRed,
                    onClick = { onUpdateType(TransactionType.EXPENSE) },
                    modifier = Modifier.weight(1f)
                )
                TransactionTypeChip(
                    label = "Transfer",
                    icon = Icons.AutoMirrored.Rounded.CompareArrows,
                    selected = uiState.selectedType == TransactionType.TRANSFER,
                    color = TealBlue,
                    onClick = { onUpdateType(TransactionType.TRANSFER) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ── Date Picker Field ──
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = Formatters.longDate(uiState.dateTime),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Tanggal Transaksi") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Rounded.CalendarToday, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }

        // ── Amount Display ──
        item {
            val amountTextFieldValue by remember(uiState.amountInput) {
                val digits = uiState.amountInput
                derivedStateOf {
                    TextFieldValue(
                        text = digits,
                        selection = TextRange(digits.length)
                    )
                }
            }

            val displayText = if (uiState.amountInput.isNotEmpty()) {
                uiState.amountInput.toLongOrNull()
                    ?.let { Formatters.rupiah(it) } ?: "Rp0"
            } else {
                "Rp0"
            }

            val amountTextStyle = MaterialTheme.typography.displayMedium.copy(
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (uiState.amountInput.isNotEmpty()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 28.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = amountTextFieldValue,
                    onValueChange = { newValue ->
                        val digits = newValue.text.filter(Char::isDigit)
                        onUpdateAmount(digits)
                    },
                    singleLine = true,
                    textStyle = amountTextStyle.copy(color = Color.Transparent),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    cursorBrush = SolidColor(Color.Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayText,
                                style = amountTextStyle
                            )
                        }
                    }
                )
            }
        }

        // ── Quick Amount Chips ──
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val quickAmounts = listOf(10_000L, 20_000L, 50_000L, 100_000L, 200_000L, 500_000L)
                items(quickAmounts) { amount ->
                    FilterChip(
                        selected = uiState.amountInput == amount.toString(),
                        onClick = { onUpdateAmount(amount.toString()) },
                        label = { Text(Formatters.rupiah(amount), style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // ── Account Dropdown ──
        item {
            FinanceDropdown(
                label = "Akun",
                selectedLabel = uiState.accounts.firstOrNull { it.id == uiState.selectedAccountId }
                    ?.name.orEmpty(),
                placeholder = "Pilih akun",
                options = uiState.accounts.map { it.id to it.name },
                onOptionSelected = onUpdateAccount
            )
        }

        // ── Transfer To / Category ──
        if (uiState.selectedType == TransactionType.TRANSFER) {
            item {
                FinanceDropdown(
                    label = "Transfer Ke",
                    selectedLabel = uiState.accounts
                        .firstOrNull { it.id == uiState.selectedTransferAccountId }?.name.orEmpty(),
                    placeholder = "Pilih akun tujuan",
                    options = uiState.accounts
                        .filter { it.id != uiState.selectedAccountId }
                        .map { it.id to it.name },
                    onOptionSelected = { onUpdateTransferAccount(it) }
                )
            }
        } else {
            item {
                FinanceDropdown(
                    label = "Kategori",
                    selectedLabel = uiState.categories
                        .firstOrNull { it.id == uiState.selectedCategoryId }?.name.orEmpty(),
                    placeholder = "Pilih kategori",
                    options = uiState.categories.map { it.id to it.name },
                    onOptionSelected = { onUpdateCategory(it) }
                )
            }
        }

        // ── Note ──
        item {
            OutlinedTextField(
                value = uiState.note,
                onValueChange = onUpdateNote,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Catatan (opsional)") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        // ── Error ──
        if (!uiState.errorMessage.isNullOrBlank()) {
            item {
                Text(
                    text = uiState.errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // ── Save Button ──
        item {
            Button(
                onClick = onSaveTransaction,
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (uiState.isSaving) "Menyimpan..." else if (isEditMode) "Update Transaksi" else "Simpan Transaksi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun TransactionTypeChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinanceDropdown(
    label: String,
    selectedLabel: String,
    placeholder: String,
    options: List<Pair<Long, String>>,
    onOptionSelected: (Long) -> Unit,
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
            label = { Text(label) },
            placeholder = { Text(placeholder) },
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
            options.forEach { (id, text) ->
                DropdownMenuItem(
                    text = { Text(text, style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        expanded = false
                        onOptionSelected(id)
                    }
                )
            }
        }
    }
}
