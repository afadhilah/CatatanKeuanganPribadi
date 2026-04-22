package com.example.catatankeuanganpribadi.data.local.model

import androidx.room.ColumnInfo

data class BudgetProgress(
    @ColumnInfo(name = "budget_id")
    val budgetId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "category_icon")
    val categoryIcon: String,
    @ColumnInfo(name = "category_color_hex")
    val categoryColorHex: String,
    @ColumnInfo(name = "limit_amount")
    val limitAmount: Long,
    @ColumnInfo(name = "used_amount")
    val usedAmount: Long
)
