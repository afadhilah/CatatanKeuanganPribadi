package com.example.catatankeuanganpribadi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.catatankeuanganpribadi.FinanceApplication
import com.example.catatankeuanganpribadi.presentation.addtransaction.AddTransactionViewModel
import com.example.catatankeuanganpribadi.presentation.budget.BudgetViewModel
import com.example.catatankeuanganpribadi.presentation.dashboard.DashboardViewModel
import com.example.catatankeuanganpribadi.presentation.management.ManageAccountsViewModel
import com.example.catatankeuanganpribadi.presentation.management.ManageCategoriesViewModel
import com.example.catatankeuanganpribadi.presentation.statistics.StatisticsViewModel
import com.example.catatankeuanganpribadi.presentation.transactionlist.TransactionListViewModel

object FinanceViewModelFactory {

    fun dashboard(): ViewModelProvider.Factory = factory { application ->
        DashboardViewModel(
            observeDashboardSummaryUseCase = application.container.observeDashboardSummaryUseCase,
            observeRecentTransactionsUseCase = application.container.observeRecentTransactionsUseCase
        )
    }

    fun addTransaction(transactionId: Long = -1L): ViewModelProvider.Factory = factory { application ->
        AddTransactionViewModel(
            transactionId = transactionId,
            accountRepository = application.container.accountRepository,
            categoryRepository = application.container.categoryRepository,
            transactionRepository = application.container.transactionRepository,
            saveTransactionUseCase = application.container.saveTransactionUseCase
        )
    }

    fun transactionList(): ViewModelProvider.Factory = factory { application ->
        TransactionListViewModel(
            accountRepository = application.container.accountRepository,
            categoryRepository = application.container.categoryRepository,
            observeFilteredTransactionsUseCase = application.container.observeFilteredTransactionsUseCase,
            deleteTransactionUseCase = application.container.deleteTransactionUseCase
        )
    }

    fun budget(): ViewModelProvider.Factory = factory { application ->
        BudgetViewModel(
            observeBudgetUsageUseCase = application.container.observeBudgetUsageUseCase,
            categoryRepository = application.container.categoryRepository,
            budgetRepository = application.container.budgetRepository
        )
    }

    fun statistics(): ViewModelProvider.Factory = factory { application ->
        StatisticsViewModel(
            observeTransactionsUseCase = application.container.observeTransactionsUseCase
        )
    }

    fun manageAccounts(): ViewModelProvider.Factory = factory { application ->
        ManageAccountsViewModel(accountRepository = application.container.accountRepository)
    }

    fun manageCategories(): ViewModelProvider.Factory = factory { application ->
        ManageCategoriesViewModel(categoryRepository = application.container.categoryRepository)
    }

    private fun <T : ViewModel> factory(
        create: (FinanceApplication) -> T
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <VM : ViewModel> create(
                modelClass: Class<VM>,
                extras: CreationExtras
            ): VM {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as FinanceApplication
                return create(application) as VM
            }
        }
    }
}
