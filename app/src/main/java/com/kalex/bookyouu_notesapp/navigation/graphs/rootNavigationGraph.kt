package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        subjectNav(rootNavController)
        recordsNav(rootNavController)
        averageNav(rootNavController)
    }
}
