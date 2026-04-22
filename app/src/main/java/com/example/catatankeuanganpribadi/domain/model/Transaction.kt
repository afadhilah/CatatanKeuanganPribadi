package com.example.catatankeuanganpribadi.domain.model

data class Transaction(
    val id: Long,
    val type: TransactionType,
    val amount: Long,
    val accountId: Long,
    val transferAccountId: Long?,
    val categoryId: Long?,
    val dateTime: Long,
    val note: String?
)
