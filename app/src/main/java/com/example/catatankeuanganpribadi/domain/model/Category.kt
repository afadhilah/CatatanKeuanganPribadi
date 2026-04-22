package com.example.catatankeuanganpribadi.domain.model

data class Category(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val icon: String,
    val colorHex: String,
    val parentCategoryId: Long?,
    val isDefault: Boolean
)
