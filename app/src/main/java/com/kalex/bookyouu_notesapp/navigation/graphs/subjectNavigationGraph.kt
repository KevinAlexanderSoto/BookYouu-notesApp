package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.subject.createsubject.SubjectForm
import com.kalex.bookyouu_notesapp.subject.SubjectMainScreen

fun NavGraphBuilder.subjectNav(rootNavController: NavHostController) {
    navigation(
        route = Route.SUBJECT,
        startDestination = Route.SUBJECT_LIST,
    ) {
        composable(route = Route.SUBJECT_LIST) {
            SubjectMainScreen(onAddNewSubject = {
                rootNavController.navigate(
                    Route.SUBJECT_FORM,
                )
            })
        }
        composable(route = Route.SUBJECT_FORM) { SubjectForm() }
        composable(route = Route.SUBJECT_DETAIL) {}
    }
}
