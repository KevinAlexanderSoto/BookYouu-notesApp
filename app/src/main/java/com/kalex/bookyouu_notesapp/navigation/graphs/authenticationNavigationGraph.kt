package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.authentication.AuthenticationMain
import com.kalex.bookyouu_notesapp.navigation.Route

fun NavGraphBuilder.authenticationGraph(rootNavController: NavHostController) {
    navigation(
        route = Route.AUTHENTICATION_ROUTE,
        startDestination = Route.AUTHENTICATION_SCREEN,
    ) {
        composable(route = Route.AUTHENTICATION_SCREEN) {
            AuthenticationMain(
                onNavigateToMainApplication = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(Route.SUBJECT)
                })
        }
    }
}