package com.example.catatankeuanganpribadi.data.local.seed

import com.example.catatankeuanganpribadi.data.local.entity.CategoryEntity
import com.example.catatankeuanganpribadi.data.local.model.FinanceType

object DefaultCategories {

    val items = listOf(
        CategoryEntity(
            name = "Gaji",
            type = FinanceType.INCOME,
            icon = "payments",
            colorHex = "#2E7D32",
            isDefault = true
        ),
        CategoryEntity(
            name = "Bonus",
            type = FinanceType.INCOME,
            icon = "redeem",
            colorHex = "#43A047",
            isDefault = true
        ),
        CategoryEntity(
            name = "Makan",
            type = FinanceType.EXPENSE,
            icon = "restaurant",
            colorHex = "#E53935",
            isDefault = true
        ),
        CategoryEntity(
            name = "Transport",
            type = FinanceType.EXPENSE,
            icon = "directions_car",
            colorHex = "#FB8C00",
            isDefault = true
        ),
        CategoryEntity(
            name = "Belanja",
            type = FinanceType.EXPENSE,
            icon = "shopping_bag",
            colorHex = "#8E24AA",
            isDefault = true
        ),
        CategoryEntity(
            name = "Hiburan",
            type = FinanceType.EXPENSE,
            icon = "movie",
            colorHex = "#3949AB",
            isDefault = true
        )
    )
}
