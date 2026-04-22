package com.example.catatankeuanganpribadi.di

import android.content.Context
import com.example.catatankeuanganpribadi.data.local.AppDatabase
import com.example.catatankeuanganpribadi.data.repository.AccountRepositoryImpl
import com.example.catatankeuanganpribadi.data.repository.BudgetRepositoryImpl
import com.example.catatankeuanganpribadi.data.repository.CategoryRepositoryImpl
import com.example.catatankeuanganpribadi.data.repository.TransactionRepositoryImpl
import com.example.catatankeuanganpribadi.domain.repository.AccountRepository
import com.example.catatankeuanganpribadi.domain.repository.BudgetRepository
import com.example.catatankeuanganpribadi.domain.repository.CategoryRepository
import com.example.catatankeuanganpribadi.domain.repository.TransactionRepository
import com.example.catatankeuanganpribadi.domain.usecase.DeleteTransactionUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveAccountsUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveBudgetUsageUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveDashboardSummaryUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveFilteredTransactionsUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveRecentTransactionsUseCase
import com.example.catatankeuanganpribadi.domain.usecase.ObserveTransactionsUseCase
import com.example.catatankeuanganpribadi.domain.usecase.SaveTransactionUseCase

class AppContainer(context: Context) {

    private val database = AppDatabase.getInstance(context)

    private val accountDao = database.accountDao()
    private val categoryDao = database.categoryDao()
    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()

    val accountRepository: AccountRepository by lazy {
        AccountRepositoryImpl(accountDao)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepositoryImpl(categoryDao)
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepositoryImpl(
            database = database,
            transactionDao = transactionDao,
            accountDao = accountDao
        )
    }

    val budgetRepository: BudgetRepository by lazy {
        BudgetRepositoryImpl(budgetDao)
    }

    val observeAccountsUseCase by lazy { ObserveAccountsUseCase(accountRepository) }
    val observeRecentTransactionsUseCase by lazy { ObserveRecentTransactionsUseCase(transactionRepository) }
    val observeTransactionsUseCase by lazy { ObserveTransactionsUseCase(transactionRepository) }
    val observeFilteredTransactionsUseCase by lazy { ObserveFilteredTransactionsUseCase(transactionRepository) }
    val observeDashboardSummaryUseCase by lazy { ObserveDashboardSummaryUseCase(transactionRepository) }
    val observeBudgetUsageUseCase by lazy { ObserveBudgetUsageUseCase(budgetRepository) }
    val saveTransactionUseCase by lazy { SaveTransactionUseCase(transactionRepository) }
    val deleteTransactionUseCase by lazy { DeleteTransactionUseCase(transactionRepository) }
}
