package com.example.catatankeuanganpribadi.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.catatankeuanganpribadi.presentation.addtransaction.AddTransactionScreen
import com.example.catatankeuanganpribadi.presentation.budget.BudgetScreen
import com.example.catatankeuanganpribadi.presentation.dashboard.DashboardScreen
import com.example.catatankeuanganpribadi.presentation.management.ManageAccountsScreen
import com.example.catatankeuanganpribadi.presentation.management.ManageCategoriesScreen
import com.example.catatankeuanganpribadi.presentation.settings.SettingsScreen
import com.example.catatankeuanganpribadi.presentation.splash.SplashScreen
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
        startDestination = FinanceDestination.Splash.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(TRANSITION_DURATION)) },
        exitTransition = { fadeOut(animationSpec = tween(TRANSITION_DURATION)) }
    ) {
        // ── Splash Screen ──
        composable(route = FinanceDestination.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(FinanceDestination.Dashboard.route) {
                        popUpTo(FinanceDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Tab screens (fade transition) ──
        composable(
            route = FinanceDestination.Dashboard.route,
            enterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
        ) {
            DashboardScreen(
                onOpenSettings = { navController.navigate(FinanceDestination.Settings.route) }
            )
        }

        composable(
            route = FinanceDestination.Transactions.route,
            enterTransition = { fadeIn(tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(tween(TRANSITION_DURATION)) }
        ) {
            TransactionListScreen(
                onEditTransaction = { id -> 
                    navController.navigate(FinanceDestination.EditTransaction.createRoute(id)) 
                }
            )
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

        // ── Settings & Management ──
        composable(route = FinanceDestination.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAccounts = { navController.navigate(FinanceDestination.ManageAccounts.route) },
                onNavigateToCategories = { navController.navigate(FinanceDestination.ManageCategories.route) }
            )
        }

        composable(route = FinanceDestination.ManageAccounts.route) {
            ManageAccountsScreen(onBack = { navController.popBackStack() })
        }

        composable(route = FinanceDestination.ManageCategories.route) {
            ManageCategoriesScreen(onBack = { navController.popBackStack() })
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

        // ── Edit Transaction (slide up) ──
        composable(
            route = FinanceDestination.EditTransaction.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType }),
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
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: -1L
            AddTransactionScreen(
                transactionId = transactionId,
                onBackAfterSave = { navController.popBackStack() }
            )
        }
    }
}
