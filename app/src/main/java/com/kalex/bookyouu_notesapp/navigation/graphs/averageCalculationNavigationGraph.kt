package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kalex.bookyouu_notesapp.average.AverageList
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.navigation.Route

fun NavGraphBuilder.averageNav(rootNavController: NavHostController) {
    navigation(
        route = Route.AVERAGE,
        startDestination = Route.AVERAGE_MAIN,
    ) {
        composable(route = Route.AVERAGE_MAIN) {
            ScaffoldBottomBar(
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBottomNavigationClick = {
                    rootNavController.navigate(it)
                },
                content = { padding ->
                    AverageList(padding)
                },
            )
        }
    }
}
