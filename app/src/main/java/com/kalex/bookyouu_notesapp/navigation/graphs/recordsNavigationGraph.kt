package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.core.camera.CameraScreen
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.core.common.encodeUri
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.ScaffoldFloatingButtonAndTopBar
import com.kalex.bookyouu_notesapp.navigation.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.records.RecordsMainScreen
import com.kalex.bookyouu_notesapp.records.createRecord.RecordReview
import com.kalex.bookyouu_notesapp.records.recordsDetails.RecordMainDetail

@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
fun NavGraphBuilder.recordsNav(rootNavController: NavHostController) {
    navigation(
        route = Route.RECORDS_PARAM_ROUTE,
        startDestination = Route.RECORDS_MAIN_SCREEN,
        arguments = listOf(
            navArgument("subjectID") {
                defaultValue = 0
                nullable = false
                type = NavType.IntType
            },
        ),
    ) {
        composable(
            route = Route.RECORDS_MAIN_SCREEN,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
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
        ) { entry ->
            val parentEntry =
                remember(entry) { rootNavController.getBackStackEntry(Route.RECORDS_PARAM_ROUTE) }
            val subjectID = parentEntry.arguments?.getInt("subjectID") ?: 0

            ScaffoldFloatingButtonAndTopBar(
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBackNavigationClick = { rootNavController.popBackStack() },
                onFloatingActionClick = { rootNavController.navigate(Route.RECORDS_CAPTURE) },
                content = {
                    RecordsMainScreen(
                        paddingValues = it,
                        subjectId = subjectID,
                        onAddNewRecord = {
                            rootNavController.navigate(Route.RECORDS_CAPTURE)
                        },
                        onRecordDetail = { noteID ->
                            rootNavController.navigate(Route.RECORDS_DETAIL_MAIN_ROUTE + "/$noteID")
                        },
                    )
                },
            )
        }
        composable(
            route = Route.RECORDS_CAPTURE,
        ) {
            var navigationFlag by remember { mutableStateOf(false) }
            var imageUri by remember { mutableStateOf("") }
            AnimatedVisibility(!navigationFlag) {
                CameraScreen() { imageCapturedUri ->
                    if (imageCapturedUri !== null) {
                        imageUri = imageCapturedUri.toString().encodeUri()
                        navigationFlag = true
                    }
                }
            }
            if (navigationFlag) {
                LaunchedEffect(key1 = Unit, block = {
                    rootNavController.navigate(Route.RECORDS_MAIN_REVIEW + "/$imageUri")
                })
            }
        }

        composable(
            route = Route.RECORDS_DETAIL_PARAM_SCREEN,
            arguments = listOf(
                navArgument("noteID") {
                    defaultValue = 0
                    nullable = false
                    type = NavType.IntType
                },
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
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
        ) { backStackEntry ->
            val recordsEntry =
                remember(backStackEntry) { rootNavController.getBackStackEntry(Route.RECORDS_DETAIL_PARAM_SCREEN) }
            val noteID = recordsEntry.arguments?.getInt("noteID") ?: 0
            ScaffoldTopBar(
                currentDestination = rootNavController.currentBackStackEntry?.destination,
                onBackNavigationClick = { rootNavController.popBackStack() },
            ) { paddingValues ->
                RecordMainDetail(noteID, paddingValues)
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
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
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
                onCaptureSaved = {
                    rootNavController.popBackStack(
                        Route.RECORDS_MAIN_SCREEN,
                        false,
                    )
                },
                onReCapture = { rootNavController.popBackStack() },
            )
        }
    }
}
