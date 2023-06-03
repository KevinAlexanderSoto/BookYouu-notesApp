package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.records.RecordsMainScreen
import com.kalex.bookyouu_notesapp.records.createRecord.presentation.RecordCapture

fun NavGraphBuilder.recordsNav(rootNavController: NavHostController) {
    navigation(
        route = Route.RECORDS_PARAM_ROUTE,
        startDestination = Route.RECORDS_LIST,
        arguments = listOf(
            navArgument("subjectID") {
                defaultValue = ""
                nullable = false
                type = NavType.StringType
            },
        ),
    ) {
        composable(
            route = Route.RECORDS_LIST,
        ) { entry ->
            val parentEntry =
                remember(entry) { rootNavController.getBackStackEntry(Route.RECORDS_PARAM_ROUTE) }
            val subjectID = parentEntry.arguments?.getString("subjectID") ?: "0" // TODO:
            RecordsMainScreen(
                subjectId = subjectID,
                onAddNewRecord = {
                    rootNavController.navigate(Route.RECORDS_CAPTURE)
                },
            )
        }
        composable(
            route = Route.RECORDS_CAPTURE,
        ) { entry ->
            val parentEntry =
                remember(entry) { rootNavController.getBackStackEntry(Route.RECORDS_PARAM_ROUTE) }
            val subjectID = parentEntry.arguments?.getString("subjectID") ?: "0" // TODO:
            RecordCapture()
        }
    }
}
