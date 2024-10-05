package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.average.AverageList
import com.kalex.bookyouu_notesapp.navigation.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.navigation.Route

fun NavGraphBuilder.averageNav(rootNavController: NavHostController) {
    navigation(
        route = Route.AVERAGE,
        startDestination = Route.AVERAGE_MAIN,
    ) {
        composable(route = Route.AVERAGE_MAIN) {
            ScaffoldBottomBar(
                currentDestination = Route.AVERAGE_MAIN,
                onBottomNavigationClick = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(it)
                },
                content = { padding ->
                    AverageList(padding)
                },
            )
        }
    }
}
