package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kalex.bookyouu_notesapp.camera.presentation.CameraScannerRoot
import com.kalex.bookyouu_notesapp.camera.presentation.CameraScannerViewModel
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.expenses.presentation.AddExpenseRoot
import com.kalex.bookyouu_notesapp.expenses.presentation.ExpenseListRoot
import com.kalex.bookyouu_notesapp.expenses.presentation.ExpenseViewModel
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.expensesNav(rootNavController: NavHostController) {
    navigation(
        route = Route.EXPENSES,
        startDestination = Route.EXPENSES_LIST
    ) {
        composable(route = Route.EXPENSES_LIST) {
            val viewModel = koinViewModel<ExpenseViewModel>()
            ScaffoldBottomBar(
                currentDestination = Route.EXPENSES_LIST,
                bottomNavigationBarScreens = BottomNavigationScreens.bottomNavItems,
                onBottomNavigationClick = {
                    rootNavController.navigate(it) {
                        popUpTo(rootNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                content = { paddingValues ->
                    ExpenseListRoot(
                        viewModel = viewModel,
                        paddingValues = paddingValues,
                        onNavigateToAddExpense = {
                            rootNavController.navigate(Route.ADD_EXPENSE.replace("{expenseId}", "-1"))
                        },
                        onNavigateToEditExpense = { id ->
                            rootNavController.navigate(Route.ADD_EXPENSE.replace("{expenseId}", id.toString()))
                        }
                    )
                })
        }

        composable(
            route = Route.ADD_EXPENSE,
            arguments = listOf(
                navArgument("expenseId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val viewModel = koinViewModel<ExpenseViewModel>()
            AddExpenseRoot(
                viewModel = viewModel,
                savedStateHandle = backStackEntry.savedStateHandle,
                onNavigateToScanReceipt = {
                    rootNavController.navigate(Route.EXPENSE_CAMERA_SCAN)
                },
                onNavigateBack = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable(route = Route.EXPENSE_CAMERA_SCAN) {
            val viewModel = koinViewModel<CameraScannerViewModel>()
            CameraScannerRoot(
                viewModel = viewModel,
                onReceiptScanned = { scannedReceipt ->
                    rootNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_receipt_amount", scannedReceipt.totalAmount?.let { String.format("%.0f", it) } ?: "")
                    rootNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_receipt_merchant", scannedReceipt.merchantName ?: "")
                    rootNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_receipt_date", scannedReceipt.date?.toString() ?: "")
                    rootNavController.popBackStack()
                },
                onNavigateBack = {
                    rootNavController.popBackStack()
                }
            )
        }
    }
}
