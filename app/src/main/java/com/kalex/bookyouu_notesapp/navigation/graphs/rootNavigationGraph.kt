package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(
    rootNavController: NavHostController,
    startDestination: String,
) {
    AnimatedNavHost(
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
