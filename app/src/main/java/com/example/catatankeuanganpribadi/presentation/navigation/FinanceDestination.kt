package com.example.catatankeuanganpribadi.presentation.navigation

sealed class FinanceDestination(val route: String) {
    data object Splash : FinanceDestination("splash")
    data object Dashboard : FinanceDestination("dashboard")
    data object AddTransaction : FinanceDestination("add_transaction")
    data object EditTransaction : FinanceDestination("edit_transaction/{transactionId}") {
        fun createRoute(id: Long) = "edit_transaction/$id"
    }
    data object Transactions : FinanceDestination("transactions")
    data object Budgets : FinanceDestination("budgets")
    data object Statistics : FinanceDestination("statistics")
    
    // Settings & Management
    data object Settings : FinanceDestination("settings")
    data object ManageAccounts : FinanceDestination("manage_accounts")
    data object ManageCategories : FinanceDestination("manage_categories")
}
