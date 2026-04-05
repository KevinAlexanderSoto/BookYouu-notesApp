package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.moreMenu.MainMoreMenu
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens

fun NavGraphBuilder.moreMenuNav(rootNavController: NavHostController) {
    navigation(
        route = Route.MORE_MENU,
        startDestination = Route.MORE_MENU_MAIN,
    ) {
        composable(route = Route.MORE_MENU_MAIN) {

            ScaffoldBottomBar(
                currentDestination = Route.MORE_MENU_MAIN,
                bottomNavigationBarScreens =  BottomNavigationScreens.bottomNavItems,
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
                    MainMoreMenu(
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            )
        }
    }
}
