package com.example.catatankeuanganpribadi.domain.repository

import com.example.catatankeuanganpribadi.domain.model.Budget
import com.example.catatankeuanganpribadi.domain.model.BudgetUsage
import com.example.catatankeuanganpribadi.domain.model.DateRange
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun observeBudgets(): Flow<List<Budget>>
    fun observeBudgetUsage(dateRange: DateRange): Flow<List<BudgetUsage>>
    suspend fun getBudgetByCategory(categoryId: Long): Budget?
    suspend fun saveBudget(budget: Budget): Long
    suspend fun deleteBudget(budgetId: Long)
}
