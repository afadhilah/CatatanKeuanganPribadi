package com.example.catatankeuanganpribadi.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.catatankeuanganpribadi.presentation.addtransaction.AddTransactionScreen
import com.example.catatankeuanganpribadi.presentation.budget.BudgetScreen
import com.example.catatankeuanganpribadi.presentation.dashboard.DashboardScreen
import com.example.catatankeuanganpribadi.presentation.statistics.StatisticsScreen
import com.example.catatankeuanganpribadi.presentation.transactionlist.TransactionListScreen

private const val TRANSITION_DURATION = 300

@Composable
fun FinanceNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FinanceDestination.Dashboard.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(TRANSITION_DURATION)) },
        exitTransition = { fadeOut(animationSpec = tween(TRANSITION_DURATION)) }
    ) {
        // ── Tab screens (fade transition) ──
        composable(
            route = FinanceDestination.Dashboard.route,
            enterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
        ) {
            DashboardScreen(
                onAddTransaction = { navController.navigate(FinanceDestination.AddTransaction.route) },
                onOpenBudget = { navController.navigate(FinanceDestination.Budgets.route) },
                onOpenStatistics = { navController.navigate(FinanceDestination.Statistics.route) }
            )
        }

        composable(
            route = FinanceDestination.Transactions.route,
            enterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
        ) {
            TransactionListScreen()
        }

        composable(
            route = FinanceDestination.Budgets.route,
            enterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
        ) {
            BudgetScreen()
        }

        composable(
            route = FinanceDestination.Statistics.route,
            enterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
        ) {
            StatisticsScreen()
        }

        // ── Add Transaction (slide up) ──
        composable(
            route = FinanceDestination.AddTransaction.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(TRANSITION_DURATION)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(TRANSITION_DURATION)
                )
            }
        ) {
            AddTransactionScreen(
                onBackAfterSave = { navController.popBackStack() }
            )
        }
    }
}
