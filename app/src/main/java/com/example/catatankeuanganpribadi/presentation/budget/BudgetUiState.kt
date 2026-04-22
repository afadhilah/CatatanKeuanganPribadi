package com.example.catatankeuanganpribadi.presentation.budget

import com.example.catatankeuanganpribadi.domain.model.BudgetUsage
import com.example.catatankeuanganpribadi.domain.model.Category
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter

data class BudgetUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val selectedPeriod: PeriodFilter = PeriodFilter.MONTH,
    val budgetUsages: List<BudgetUsage> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Long? = null,
    val limitAmountInput: String = "",
    val errorMessage: String? = null
)
