package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.material3.Scaffold
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.moreMenu.MainMoreMenu
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.records.createRecord.RecordReview

fun NavGraphBuilder.moreMenuNav(rootNavController: NavHostController) {
    navigation(
        route = Route.MORE_MENU,
        startDestination = Route.MORE_MENU_MAIN,
    ) {
        composable(route = Route.MORE_MENU_MAIN) {

            ScaffoldBottomBar(currentDestination = Route.MORE_MENU_MAIN, onBottomNavigationClick = {
                rootNavController.popBackStack()
                rootNavController.navigate(it)
            }, content = {
                Scaffold() { _ ->
                    MainMoreMenu()
                }
            })
        }
    }
}