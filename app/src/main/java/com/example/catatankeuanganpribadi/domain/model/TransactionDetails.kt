package com.example.catatankeuanganpribadi.domain.model

data class TransactionDetails(
    val id: Long,
    val type: TransactionType,
    val amount: Long,
    val dateTime: Long,
    val note: String?,
    val accountId: Long,
    val accountName: String,
    val accountType: AccountType,
    val transferAccountId: Long?,
    val transferAccountName: String?,
    val categoryId: Long?,
    val categoryName: String?,
    val categoryIcon: String?,
    val categoryColorHex: String?
)
