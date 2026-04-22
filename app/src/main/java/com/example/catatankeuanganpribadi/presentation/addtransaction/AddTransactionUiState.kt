package com.example.catatankeuanganpribadi.presentation.addtransaction

import com.example.catatankeuanganpribadi.domain.model.Account
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.domain.model.TransactionType

data class AddTransactionUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val amountInput: String = "",
    val selectedAccountId: Long? = null,
    val selectedTransferAccountId: Long? = null,
    val selectedCategoryId: Long? = null,
    val dateTime: Long = System.currentTimeMillis(),
    val note: String = "",
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val errorMessage: String? = null,
    val saveCompleted: Boolean = false
)
