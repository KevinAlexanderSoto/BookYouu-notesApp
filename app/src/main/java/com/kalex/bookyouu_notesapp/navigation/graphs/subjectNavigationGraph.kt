package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.material3.Scaffold
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kalex.bookyouu_notesapp.navigation.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.navigation.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.subject.SubjectMainScreen
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui.BYSuccessScreen
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui.SubjectForm

fun NavGraphBuilder.subjectNav(rootNavController: NavHostController) {
    navigation(
        route = Route.SUBJECT,
        startDestination = Route.SUBJECT_LIST,
    ) {
        composable(route = Route.SUBJECT_LIST) {
            ScaffoldBottomBar(
                currentDestination = Route.SUBJECT_LIST,
                onBottomNavigationClick = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(it)
                },
                content = {
                    SubjectMainScreen(
                        onAddNewSubject = {
                            rootNavController.navigate(
                                Route.SUBJECT_FORM,
                            )
                        },
                        onSubjectClickAction = {
                            rootNavController.navigate(
                                route = Route.RECORDS_MAIN_ROUTE + "/$it",
                            )
                        },
                    )
                },
            )
        }
        composable(
            route = Route.SUBJECT_FORM,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = snap())
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700),
                )
            },
        ) {
            ScaffoldTopBar(
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBackNavigationClick = { rootNavController.popBackStack() },
                content = {
                    SubjectForm(
                        paddingValues = it,
                        onNavigateToConfirmationScreen = {
                            rootNavController.navigate(
                                Route.SUBJECT_FORM_SUCCESS_SCREEN,
                            )
                        },
                    )
                },
            )
        }
        composable(route = Route.SUBJECT_FORM_SUCCESS_SCREEN) {
            Scaffold() { _ ->
                BYSuccessScreen(
                    onNavigateToSubjectListScreen = {
                        rootNavController.popBackStack(Route.SUBJECT_LIST, false)
                    },
                )
            }

        }
    }
}
