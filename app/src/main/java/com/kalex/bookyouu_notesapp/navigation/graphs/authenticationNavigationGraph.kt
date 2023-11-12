package com.kalex.bookyouu_notesapp.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.kalex.bookyouu_notesapp.authentication.AuthenticationMain
import com.kalex.bookyouu_notesapp.navigation.Route

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authenticationGraph(rootNavController: NavHostController) {
    navigation(
        route = Route.AUTHENTICATION_ROUTE,
        startDestination = Route.AUTHENTICATION_SCREEN,
    ) {
        composable(route = Route.AUTHENTICATION_SCREEN) {
            com.kalex.bookyouu_notesapp.authentication.AuthenticationMain(
                onNavigateToMainApplication = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(Route.SUBJECT)
                })
        }
    }
}