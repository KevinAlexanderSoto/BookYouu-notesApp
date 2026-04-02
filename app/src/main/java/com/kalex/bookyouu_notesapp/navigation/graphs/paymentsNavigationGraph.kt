package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.payments.presentation.ObligationsScreen

fun NavGraphBuilder.paymentsNav(rootNavController: NavHostController) {
    navigation(
        route = Route.PAYMENTS,
        startDestination = Route.PAYMENTS_MAIN,
    ) {
        composable(route = Route.PAYMENTS_MAIN) {
            ScaffoldBottomBar(
                currentDestination = Route.PAYMENTS_MAIN,
                onBottomNavigationClick = {
                    rootNavController.navigate(it) {
                        popUpTo(rootNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                content = {
                    ObligationsScreen(
                        onMenuClick = {
                            // TODO: Open drawer if any
                        }
                    )
                },
            )
        }
    }
}
