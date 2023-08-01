package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.kalex.bookyouu_notesapp.average.AverageList
import com.kalex.bookyouu_notesapp.navigation.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.navigation.Route

@OptIn(ExperimentalAnimationApi::class)
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
