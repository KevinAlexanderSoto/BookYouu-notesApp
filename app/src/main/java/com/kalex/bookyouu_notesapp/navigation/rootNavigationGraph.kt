package com.kalex.bookyouu_notesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavigationGraph(
    rootNavController: NavHostController,
    modifier: Modifier,
    startDestination: String
) {
    NavHost(
        navController = rootNavController,
        startDestination = startDestination,
        modifier = modifier
    ){
        subjectNav(rootNavController)
    }
}

