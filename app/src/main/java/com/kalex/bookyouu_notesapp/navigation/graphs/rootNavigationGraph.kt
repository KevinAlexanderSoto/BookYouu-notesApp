package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavigationGraph(
    rootNavController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = rootNavController,
        startDestination = startDestination,
    ) {
        authenticationGraph(rootNavController)
        subjectNav(rootNavController)
        recordsNav(rootNavController)
        averageNav(rootNavController)
        moreMenuNav(rootNavController)
    }
}
