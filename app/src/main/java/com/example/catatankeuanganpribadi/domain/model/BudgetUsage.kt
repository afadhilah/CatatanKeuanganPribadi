package com.example.catatankeuanganpribadi.domain.model

data class BudgetUsage(
    val budgetId: Long,
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColorHex: String,
    val limitAmount: Long,
    val usedAmount: Long,
    val progress: Float,
    val status: BudgetStatus
)
