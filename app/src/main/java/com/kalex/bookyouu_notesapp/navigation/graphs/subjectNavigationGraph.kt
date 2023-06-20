package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.subject.SubjectMainScreen
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui.BYSuccessScreen
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui.SubjectForm

@OptIn(ExperimentalAnimationApi::class)
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
            BYSuccessScreen(
                onNavigateToSubjectListScreen = {
                    rootNavController.navigate(
                        Route.SUBJECT_LIST,
                    )
                },
            )
        }
    }
}
