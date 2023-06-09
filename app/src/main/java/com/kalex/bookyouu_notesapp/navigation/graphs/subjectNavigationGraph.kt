package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldTopBar
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
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBottomNavigationClick = {
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
        composable(route = Route.SUBJECT_FORM) {
            ScaffoldTopBar(
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBackNavigationClick = { rootNavController.popBackStack() },
                content = {
                    SubjectForm(
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
