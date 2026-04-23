package com.example.catatankeuanganpribadi.presentation.management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.presentation.FinanceViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoriesScreen(
    onBack: () -> Unit,
    viewModel: ManageCategoriesViewModel = viewModel(factory = FinanceViewModelFactory.manageCategories())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    if (showDialog) {
        CategoryDialog(
            category = selectedCategory,
            onDismiss = {
                showDialog = false
                selectedCategory = null
            },
            onConfirm = { name, type ->
                viewModel.saveCategory(selectedCategory?.id ?: 0L, name, type)
                showDialog = false
                selectedCategory = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Kategori", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedCategory = null
                showDialog = true
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Tambah Kategori")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val expenseCategories = uiState.categories.filter { it.type == TransactionType.EXPENSE }
                val incomeCategories = uiState.categories.filter { it.type == TransactionType.INCOME }

                if (incomeCategories.isNotEmpty()) {
                    item { Text("Pemasukan", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary) }
                    items(incomeCategories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = {
                                selectedCategory = category
                                showDialog = true
                            },
                            onDelete = { viewModel.deleteCategory(category.id) }
                        )
                    }
                }

                if (expenseCategories.isNotEmpty()) {
                    item { Spacer(Modifier.height(8.dp)) }
                    item { Text("Pengeluaran", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error) }
                    items(expenseCategories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = {
                                selectedCategory = category
                                showDialog = true
                            },
                            onDelete = { viewModel.deleteCategory(category.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                category.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onEdit) {
                Icon(Icons.Rounded.Edit, contentDescription = "Edit")
            }
            if (!category.isDefault) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (String, TransactionType) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var type by remember { mutableStateOf(category?.type ?: TransactionType.EXPENSE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Tambah Kategori" else "Edit Kategori") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Kategori") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text("Tipe Transaksi", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = type == TransactionType.INCOME,
                        onClick = { type = TransactionType.INCOME },
                        label = { Text("Pemasukan") }
                    )
                    FilterChip(
                        selected = type == TransactionType.EXPENSE,
                        onClick = { type = TransactionType.EXPENSE },
                        label = { Text("Pengeluaran") }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, type) },
                enabled = name.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
