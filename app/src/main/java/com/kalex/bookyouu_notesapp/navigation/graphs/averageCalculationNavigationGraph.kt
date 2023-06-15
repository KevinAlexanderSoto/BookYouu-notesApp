package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.kalex.bookyouu_notesapp.average.AverageList
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldBottomBar
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
                    rootNavController.navigate(it)
                },
                content = { padding ->
                    AverageList(padding)
                },
            )
        }
    }
}
