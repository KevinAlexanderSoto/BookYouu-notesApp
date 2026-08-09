package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.investments.presentation.AddInvestmentScreen
import com.kalex.bookyouu_notesapp.investments.presentation.InvestmentsPortfolioScreen
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavGraphBuilder.investmentsNav(rootNavController: NavHostController) {
    navigation(
        route = Route.INVESTMENTS,
        startDestination = Route.INVESTMENTS_LIST,
    ) {
        composable(route = Route.INVESTMENTS_LIST) {
            ScaffoldBottomBar(
                currentDestination = Route.INVESTMENTS_LIST,
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
                    InvestmentsPortfolioScreen(
                        paddingValues = paddingValues,
                        onNavigateToAddInvestment = {
                            rootNavController.navigate(Route.ADD_INVESTMENT)
                        }
                    )
                },
            )
        }
        composable(
            route = Route.ADD_INVESTMENT,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(500),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(500),
                )
            }
        ) {
            AddInvestmentScreen(
                onBackClick = { rootNavController.popBackStack() },
                onSuccess = { rootNavController.popBackStack() }
            )
        }
    }
}
