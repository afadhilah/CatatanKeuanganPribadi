package com.example.catatankeuanganpribadi.domain.model

data class SaveTransactionRequest(
    val id: Long = 0L,
    val type: TransactionType,
    val amount: Long,
    val accountId: Long,
    val transferAccountId: Long? = null,
    val categoryId: Long? = null,
    val dateTime: Long,
    val note: String? = null
)
