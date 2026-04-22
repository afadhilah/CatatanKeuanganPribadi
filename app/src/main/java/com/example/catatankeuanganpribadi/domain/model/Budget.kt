package com.example.catatankeuanganpribadi.domain.model

data class Budget(
    val id: Long,
    val categoryId: Long,
    val limitAmount: Long,
    val period: BudgetPeriod
)
