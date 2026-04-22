package com.example.catatankeuanganpribadi.data.repository

import com.example.catatankeuanganpribadi.data.local.dao.BudgetDao
import com.example.catatankeuanganpribadi.data.mapper.toDomain
import com.example.catatankeuanganpribadi.data.mapper.toEntity
import com.example.catatankeuanganpribadi.domain.model.Budget
import com.example.catatankeuanganpribadi.domain.model.BudgetUsage
import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun observeBudgets(): Flow<List<Budget>> {
        return budgetDao.observeBudgets().map { budgets -> budgets.map { it.toDomain() } }
    }

    override fun observeBudgetUsage(dateRange: DateRange): Flow<List<BudgetUsage>> {
        return budgetDao.observeBudgetProgress(
            startDateTime = dateRange.start,
            endDateTime = dateRange.end
        ).map { items -> items.map { it.toDomain() } }
    }

    override suspend fun getBudgetByCategory(categoryId: Long): Budget? {
        return budgetDao.getBudgetByCategoryId(categoryId)?.toDomain()
    }

    override suspend fun saveBudget(budget: Budget): Long {
        return budgetDao.upsertBudget(budget.toEntity())
    }

    override suspend fun deleteBudget(budgetId: Long) {
        val budget = budgetDao.getBudgetById(budgetId)
        if (budget != null) {
            budgetDao.deleteBudget(budget)
        }
    }
}
