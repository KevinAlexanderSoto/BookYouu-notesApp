package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kalex.bookyouu_notesapp.Greeting
import com.kalex.bookyouu_notesapp.navigation.Route

fun NavGraphBuilder.averageNav(rootNavController: NavHostController) {
    navigation(
        route = Route.AVERAGE,
        startDestination = Route.AVERAGE_MAIN,
    ) {
        composable(route = Route.AVERAGE_MAIN) { Greeting("Average") }

    }
}