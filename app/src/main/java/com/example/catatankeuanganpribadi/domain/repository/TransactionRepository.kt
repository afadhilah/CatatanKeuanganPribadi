package com.example.catatankeuanganpribadi.domain.repository

import com.example.catatankeuanganpribadi.domain.model.DashboardSummary
import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.domain.model.SaveTransactionRequest
import com.example.catatankeuanganpribadi.domain.model.Transaction
import com.example.catatankeuanganpribadi.domain.model.TransactionDetails
import com.example.catatankeuanganpribadi.domain.model.TransactionFilter
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeTransactions(dateRange: DateRange): Flow<List<TransactionDetails>>
    fun observeRecentTransactions(limit: Int): Flow<List<TransactionDetails>>
    fun observeFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionDetails>>
    fun observeDashboardSummary(dateRange: DateRange): Flow<DashboardSummary>
    suspend fun getTransaction(transactionId: Long): Transaction?
    suspend fun saveTransaction(request: SaveTransactionRequest): Long
    suspend fun deleteTransaction(transactionId: Long)
}
