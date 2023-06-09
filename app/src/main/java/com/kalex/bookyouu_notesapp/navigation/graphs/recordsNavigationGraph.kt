package com.kalex.bookyouu_notesapp.navigation.graphs

import android.net.Uri
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.camera.CameraScreen
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.records.RecordsMainScreen
import com.kalex.bookyouu_notesapp.records.createRecord.presentation.RecordReview

@OptIn(ExperimentalPermissionsApi::class)
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

            ScaffoldTopBar(
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBackNavigationClick = { rootNavController.popBackStack() },
                content = {
                    RecordsMainScreen(
                        paddingValues = it,
                        subjectId = subjectID,
                        onAddNewRecord = {
                            rootNavController.navigate(Route.RECORDS_CAPTURE)
                        },
                    )
                },
            )
        }
        composable(
            route = Route.RECORDS_CAPTURE,
        ) {
            CameraScreen() {
                if (it !== null) {
                    val encoded = Uri.encode(it.toString().replace('%', '|'))
                    rootNavController.navigate(Route.RECORDS_MAIN_REVIEW + "/$encoded")
                }
            }
        }

        composable(
            route = Route.RECORDS_PARAM_REVIEW,
            arguments = listOf(
                navArgument("photoUri") {
                    defaultValue = ""
                    nullable = false
                    type = NavType.StringType
                },
            ),
        ) { entry ->
            val parentEntry =
                remember(entry) { rootNavController.getBackStackEntry(Route.RECORDS_PARAM_ROUTE) }
            val recordsEntry =
                remember(entry) { rootNavController.getBackStackEntry(Route.RECORDS_PARAM_REVIEW) }
            val subjectID = parentEntry.arguments?.getString("subjectID") ?: "0" // TODO:
            val photoUri = recordsEntry.arguments?.let {
                it.getString("photoUri")
                    ?.replace('|', '%')
                    ?.let(Uri::parse)
            }
            RecordReview(
                subjectId = subjectID,
                captureUri = photoUri!!,
            )
        }
    }
}
