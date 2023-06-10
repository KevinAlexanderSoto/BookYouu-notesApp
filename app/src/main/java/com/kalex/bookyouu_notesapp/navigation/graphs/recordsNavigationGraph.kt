package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.camera.CameraScreen
import com.kalex.bookyouu_notesapp.common.composables.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.common.decodeUri
import com.kalex.bookyouu_notesapp.common.encodeUri
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
                defaultValue = 0
                nullable = false
                type = NavType.IntType
            },
        ),
    ) {
        composable(
            route = Route.RECORDS_LIST,
        ) { entry ->
            val parentEntry =
                remember(entry) { rootNavController.getBackStackEntry(Route.RECORDS_PARAM_ROUTE) }
            val subjectID = parentEntry.arguments?.getInt("subjectID") ?: 0

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
                    val encoded = it.toString().encodeUri()
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
            val subjectID = parentEntry.arguments?.getInt("subjectID") ?: 0
            val photoUri = recordsEntry.arguments?.let {
                it.getString("photoUri")?.decodeUri()
            }
            RecordReview(
                subjectId = subjectID,
                captureUri = photoUri!!,
            ) {
                rootNavController.popBackStack()
            }
        }
    }
}
