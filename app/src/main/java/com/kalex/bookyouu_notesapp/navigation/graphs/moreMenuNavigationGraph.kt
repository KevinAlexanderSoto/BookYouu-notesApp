package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.kalex.bookyouu_notesapp.average.AverageList
import com.kalex.bookyouu_notesapp.moreMenu.MainMoreMenu
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.subject.SubjectMainScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.moreMenuNav(rootNavController: NavHostController) {
    navigation(
        route = Route.MORE_MENU,
        startDestination = Route.MORE_MENU_MAIN,
    ) {
        composable(route = Route.MORE_MENU_MAIN) {
            ScaffoldBottomBar(
                currentDestination = Route.MORE_MENU_MAIN,
                onBottomNavigationClick = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(it)
                },
                content = {
                    MainMoreMenu()
                }
            )
        }
    }
}