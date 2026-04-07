package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
                            rootNavController.navigate(Route.ADD_EXPENSE)
                        }
                    )
                })
        }

        composable(route = Route.ADD_EXPENSE) {
            val viewModel = koinViewModel<ExpenseViewModel>()
            AddExpenseRoot(
                viewModel = viewModel,
                onNavigateBack = {
                    rootNavController.popBackStack()
                }
            )
        }
    }
}
