package com.example.catatankeuanganpribadi.data.repository

import androidx.room.withTransaction
import com.example.catatankeuanganpribadi.data.local.AppDatabase
import com.example.catatankeuanganpribadi.data.local.dao.AccountDao
import com.example.catatankeuanganpribadi.data.local.dao.TransactionDao
import com.example.catatankeuanganpribadi.data.mapper.toDomain
import com.example.catatankeuanganpribadi.data.mapper.toEntity
import com.example.catatankeuanganpribadi.domain.model.DashboardSummary
import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.domain.model.SaveTransactionRequest
import com.example.catatankeuanganpribadi.domain.model.Transaction
import com.example.catatankeuanganpribadi.domain.model.TransactionDetails
import com.example.catatankeuanganpribadi.domain.model.TransactionFilter
import com.example.catatankeuanganpribadi.domain.model.TransactionType
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val database: AppDatabase,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : TransactionRepository {

    override fun observeTransactions(dateRange: DateRange): Flow<List<TransactionDetails>> {
        return transactionDao.observeTransactions(
            startDateTime = dateRange.start,
            endDateTime = dateRange.end
        ).map { transactions -> transactions.map { it.toDomain() } }
    }

    override fun observeRecentTransactions(limit: Int): Flow<List<TransactionDetails>> {
        return transactionDao.observeRecentTransactions(limit)
            .map { transactions -> transactions.map { it.toDomain() } }
    }

    override fun observeFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionDetails>> {
        return transactionDao.observeFilteredTransactions(
            startDateTime = filter.dateRange.start,
            endDateTime = filter.dateRange.end,
            accountId = filter.accountId,
            categoryId = filter.categoryId,
            searchQuery = filter.searchQuery,
            sortByAmount = filter.sortOption == com.example.catatankeuanganpribadi.domain.model.TransactionSortOption.AMOUNT_DESC
        ).map { transactions -> transactions.map { it.toDomain() } }
    }

    override fun observeDashboardSummary(dateRange: DateRange): Flow<DashboardSummary> {
        return combine(
            accountDao.observeTotalBalance(),
            transactionDao.observeIncomeTotal(dateRange.start, dateRange.end),
            transactionDao.observeExpenseTotal(dateRange.start, dateRange.end)
        ) { totalBalance, totalIncome, totalExpense ->
            DashboardSummary(
                totalBalance = totalBalance,
                totalIncome = totalIncome,
                totalExpense = totalExpense
            )
        }
    }

    override suspend fun getTransaction(transactionId: Long): Transaction? {
        return transactionDao.getTransactionById(transactionId)?.toDomain()
    }

    override suspend fun saveTransaction(request: SaveTransactionRequest): Long {
        validateTransactionRequest(request)

        return database.withTransaction {
            if (request.id != 0L) {
                val previousTransaction = requireNotNull(transactionDao.getTransactionById(request.id)) {
                    "Transaction with id ${request.id} not found"
                }
                applyReverseBalanceEffect(previousTransaction.toDomain())
            }

            applyNewBalanceEffect(request)
            transactionDao.upsertTransaction(request.toEntity())
        }
    }

    override suspend fun deleteTransaction(transactionId: Long) {
        database.withTransaction {
            val transaction = requireNotNull(transactionDao.getTransactionById(transactionId)) {
                "Transaction with id $transactionId not found"
            }
            applyReverseBalanceEffect(transaction.toDomain())
            transactionDao.deleteTransaction(transaction)
        }
    }

    private suspend fun applyNewBalanceEffect(request: SaveTransactionRequest) {
        when (request.type) {
            TransactionType.INCOME -> adjustAccountBalance(request.accountId, request.amount)
            TransactionType.EXPENSE -> adjustAccountBalance(request.accountId, -request.amount)
            TransactionType.TRANSFER -> {
                val destinationAccountId = requireNotNull(request.transferAccountId) {
                    "Transfer destination account is required"
                }
                adjustAccountBalance(request.accountId, -request.amount)
                adjustAccountBalance(destinationAccountId, request.amount)
            }
        }
    }

    private suspend fun applyReverseBalanceEffect(transaction: Transaction) {
        when (transaction.type) {
            TransactionType.INCOME -> adjustAccountBalance(transaction.accountId, -transaction.amount)
            TransactionType.EXPENSE -> adjustAccountBalance(transaction.accountId, transaction.amount)
            TransactionType.TRANSFER -> {
                val destinationAccountId = requireNotNull(transaction.transferAccountId) {
                    "Transfer destination account is required"
                }
                adjustAccountBalance(transaction.accountId, transaction.amount)
                adjustAccountBalance(destinationAccountId, -transaction.amount)
            }
        }
    }

    private suspend fun adjustAccountBalance(accountId: Long, delta: Long) {
        val account = requireNotNull(accountDao.getAccountById(accountId)) {
            "Account with id $accountId not found"
        }
        accountDao.updateBalance(accountId, account.balance + delta)
    }

    private fun validateTransactionRequest(request: SaveTransactionRequest) {
        require(request.amount > 0) { "Amount must be greater than zero" }
        when (request.type) {
            TransactionType.INCOME,
            TransactionType.EXPENSE -> {
                require(request.categoryId != null) { "Category is required for income and expense" }
                require(request.transferAccountId == null) { "Transfer account must be empty for non-transfer transactions" }
            }
            TransactionType.TRANSFER -> {
                require(request.transferAccountId != null) { "Transfer account is required" }
                require(request.transferAccountId != request.accountId) { "Transfer accounts must be different" }
            }
        }
    }
}
