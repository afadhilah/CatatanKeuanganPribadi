package com.example.catatankeuanganpribadi.presentation.navigation

sealed class FinanceDestination(val route: String) {
    data object Dashboard : FinanceDestination("dashboard")
    data object AddTransaction : FinanceDestination("add_transaction")
    data object Transactions : FinanceDestination("transactions")
    data object Budgets : FinanceDestination("budgets")
    data object Statistics : FinanceDestination("statistics")
}
