package com.example.catatankeuanganpribadi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.catatankeuanganpribadi.data.local.entity.BudgetEntity
import com.example.catatankeuanganpribadi.data.local.model.BudgetProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets ORDER BY category_id ASC")
    fun observeBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE category_id = :categoryId LIMIT 1")
    suspend fun getBudgetByCategoryId(categoryId: Long): BudgetEntity?

    @Query("SELECT * FROM budgets WHERE id = :budgetId LIMIT 1")
    suspend fun getBudgetById(budgetId: Long): BudgetEntity?

    @Upsert
    suspend fun upsertBudget(budget: BudgetEntity): Long

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query(
        """
        SELECT
            b.id AS budget_id,
            c.id AS category_id,
            c.name AS category_name,
            c.icon AS category_icon,
            c.color_hex AS category_color_hex,
            b.limit_amount,
            COALESCE(SUM(t.amount), 0) AS used_amount
        FROM budgets b
        INNER JOIN categories c ON c.id = b.category_id
        LEFT JOIN transactions t
            ON t.category_id = b.category_id
           AND t.type = 'EXPENSE'
           AND t.date_time BETWEEN :startDateTime AND :endDateTime
        GROUP BY b.id, c.id, c.name, c.icon, c.color_hex, b.limit_amount
        ORDER BY used_amount DESC
        """
    )
    fun observeBudgetProgress(
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<BudgetProgress>>
}
