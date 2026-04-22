package com.example.catatankeuanganpribadi.domain.model

data class Account(
    val id: Long,
    val name: String,
    val type: AccountType,
    val balance: Long,
    val createdAt: Long
)
