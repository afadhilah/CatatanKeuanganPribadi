package com.example.catatankeuanganpribadi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.catatankeuanganpribadi.data.local.entity.TransactionEntity
import com.example.catatankeuanganpribadi.data.local.model.TransactionWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query(
        """
        SELECT
            t.id,
            t.type,
            t.amount,
            t.date_time,
            t.note,
            t.account_id,
            source.name AS account_name,
            source.type AS account_type,
            t.transfer_account_id,
            destination.name AS transfer_account_name,
            t.category_id,
            c.name AS category_name,
            c.icon AS category_icon,
            c.color_hex AS category_color_hex
        FROM transactions t
        INNER JOIN accounts source ON source.id = t.account_id
        LEFT JOIN accounts destination ON destination.id = t.transfer_account_id
        LEFT JOIN categories c ON c.id = t.category_id
        WHERE t.date_time BETWEEN :startDateTime AND :endDateTime
        ORDER BY t.date_time DESC
        """
    )
    fun observeTransactions(
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<TransactionWithDetails>>

    @Query(
        """
        SELECT
            t.id,
            t.type,
            t.amount,
            t.date_time,
            t.note,
            t.account_id,
            source.name AS account_name,
            source.type AS account_type,
            t.transfer_account_id,
            destination.name AS transfer_account_name,
            t.category_id,
            c.name AS category_name,
            c.icon AS category_icon,
            c.color_hex AS category_color_hex
        FROM transactions t
        INNER JOIN accounts source ON source.id = t.account_id
        LEFT JOIN accounts destination ON destination.id = t.transfer_account_id
        LEFT JOIN categories c ON c.id = t.category_id
        ORDER BY t.date_time DESC
        LIMIT :limit
        """
    )
    fun observeRecentTransactions(limit: Int): Flow<List<TransactionWithDetails>>

    @Query(
        """
        SELECT
            t.id,
            t.type,
            t.amount,
            t.date_time,
            t.note,
            t.account_id,
            source.name AS account_name,
            source.type AS account_type,
            t.transfer_account_id,
            destination.name AS transfer_account_name,
            t.category_id,
            c.name AS category_name,
            c.icon AS category_icon,
            c.color_hex AS category_color_hex
        FROM transactions t
        INNER JOIN accounts source ON source.id = t.account_id
        LEFT JOIN accounts destination ON destination.id = t.transfer_account_id
        LEFT JOIN categories c ON c.id = t.category_id
        WHERE (:accountId IS NULL OR t.account_id = :accountId OR t.transfer_account_id = :accountId)
          AND (:categoryId IS NULL OR t.category_id = :categoryId)
          AND t.date_time BETWEEN :startDateTime AND :endDateTime
          AND (
              :searchQuery IS NULL OR :searchQuery = '' OR
              t.note LIKE '%' || :searchQuery || '%' OR
              c.name LIKE '%' || :searchQuery || '%' OR
              source.name LIKE '%' || :searchQuery || '%'
          )
        ORDER BY
          CASE WHEN :sortByAmount = 1 THEN t.amount END DESC,
          CASE WHEN :sortByAmount = 0 THEN t.date_time END DESC
        """
    )
    fun observeFilteredTransactions(
        startDateTime: Long,
        endDateTime: Long,
        accountId: Long?,
        categoryId: Long?,
        searchQuery: String?,
        sortByAmount: Boolean
    ): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity): Long

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT COUNT(*) FROM transactions WHERE account_id = :accountId OR transfer_account_id = :accountId")
    suspend fun countTransactionsUsingAccount(accountId: Long): Int

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'INCOME'
          AND date_time BETWEEN :startDateTime AND :endDateTime
        """
    )
    fun observeIncomeTotal(startDateTime: Long, endDateTime: Long): Flow<Long>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'EXPENSE'
          AND date_time BETWEEN :startDateTime AND :endDateTime
        """
    )
    fun observeExpenseTotal(startDateTime: Long, endDateTime: Long): Flow<Long>
}
