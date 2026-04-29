package com.kalex.bookyouu_notesapp.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.journal.R
import com.kalex.bookyouu_notesapp.core.camera.CameraScreen
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldBottomBar
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldFloatingButtonAndTopBar
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.core.common.composables.SuccessStatusScreen
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.core.common.encodeUri
import com.kalex.bookyouu_notesapp.journal.JournalMainScreen
import com.kalex.bookyouu_notesapp.journal.createJournal.presentation.ui.JournalFormScreen
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.ui.JournalEntryMainScreen
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.ui.JournalEntryReview
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.ui.JournalEntryMainDetail
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens
import com.kalex.bookyouu_notesapp.navigation.topBar.TopBarTitleFactory

fun NavGraphBuilder.journalNav(rootNavController: NavHostController) {
    navigation(
        route = Route.JOURNAL,
        startDestination = Route.JOURNAL_LIST,
    ) {
        composable(route = Route.JOURNAL_LIST) {
            ScaffoldBottomBar(
                currentDestination = Route.JOURNAL_LIST,
                bottomNavigationBarScreens = BottomNavigationScreens.bottomNavItems,
                onBottomNavigationClick = {
                    rootNavController.navigate(it) {
                        popUpTo(rootNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                content = {
                    JournalMainScreen(
                        onAddNewJournal = {
                            rootNavController.navigate(Route.JOURNAL_FORM)
                        },
                        onJournalClickAction = {
                            rootNavController.navigate(route = Route.JOURNAL_ENTRY_MAIN_ROUTE + "/$it")
                        },
                    )
                },
            )
        }
        composable(
            route = Route.JOURNAL_FORM,
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
        ) { entry ->
            ScaffoldTopBar(
                title = stringResource(TopBarTitleFactory.getTopBarTitle(entry.destination.route)),
                onBackNavigationClick = { rootNavController.popBackStack() },
                content = {
                    JournalFormScreen(
                        paddingValues = it,
                        onNavigateToConfirmationScreen = {
                            rootNavController.navigate(Route.JOURNAL_FORM_SUCCESS_SCREEN)
                        },
                    )
                },
            )
        }
        composable(route = Route.JOURNAL_FORM_SUCCESS_SCREEN) {
            Scaffold { _ ->
                SuccessStatusScreen(
                    title = stringResource(R.string.success_screen_text),
                    message = "",
                    primaryButtonText = stringResource(R.string.success_screen_buttom_text),
                    onPrimaryClick = {
                        rootNavController.popBackStack(Route.JOURNAL_LIST, false)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.journalEntryNav(rootNavController: NavHostController) {
    navigation(
        route = Route.JOURNAL_ENTRY_PARAM_ROUTE,
        startDestination = Route.JOURNAL_ENTRY_MAIN_SCREEN,
        arguments = listOf(
            navArgument("journalId") {
                defaultValue = 0
                nullable = false
                type = NavType.IntType
            },
        ),
    ) {
        composable(
            route = Route.JOURNAL_ENTRY_MAIN_SCREEN,
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
            val parentEntry = remember(entry) { rootNavController.getBackStackEntry(Route.JOURNAL_ENTRY_PARAM_ROUTE) }
            val journalId = parentEntry.arguments?.getInt("journalId") ?: 0

            ScaffoldFloatingButtonAndTopBar(
                title = stringResource(TopBarTitleFactory.getTopBarTitle(entry.destination.route)),
                onBackNavigationClick = { rootNavController.popBackStack() },
                onFloatingActionClick = { rootNavController.navigate(Route.JOURNAL_ENTRY_CAPTURE) },
                content = {
                    JournalEntryMainScreen(
                        paddingValues = it,
                        journalId = journalId,
                        onAddNewEntry = {
                            rootNavController.navigate(Route.JOURNAL_ENTRY_CAPTURE)
                        },
                        onEntryDetail = { entryId ->
                            rootNavController.navigate(Route.JOURNAL_ENTRY_DETAIL_MAIN_ROUTE + "/$entryId")
                        },
                    )
                },
            )
        }
        composable(route = Route.JOURNAL_ENTRY_CAPTURE) {
            var navigationFlag by remember { mutableStateOf(false) }
            var imageUri by remember { mutableStateOf("") }
            AnimatedVisibility(!navigationFlag) {
                CameraScreen { imageCapturedUri ->
                    if (imageCapturedUri != null) {
                        imageUri = imageCapturedUri.toString().encodeUri()
                        navigationFlag = true
                    }
                }
            }
            if (navigationFlag) {
                LaunchedEffect(key1 = Unit) {
                    rootNavController.navigate(Route.JOURNAL_ENTRY_MAIN_REVIEW + "/$imageUri")
                }
            }
        }

        composable(
            route = Route.JOURNAL_ENTRY_DETAIL_PARAM_SCREEN,
            arguments = listOf(
                navArgument("entryId") {
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
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: 0
            ScaffoldTopBar(
                title = stringResource(TopBarTitleFactory.getTopBarTitle(backStackEntry.destination.route)),
                onBackNavigationClick = { rootNavController.popBackStack() },
            ) { paddingValues ->
                JournalEntryMainDetail(entryId, paddingValues)
            }
        }

        composable(
            route = Route.JOURNAL_ENTRY_PARAM_REVIEW,
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
            val parentEntry = remember(entry) { rootNavController.getBackStackEntry(Route.JOURNAL_ENTRY_PARAM_ROUTE) }
            val reviewEntry = remember(entry) { rootNavController.getBackStackEntry(Route.JOURNAL_ENTRY_PARAM_REVIEW) }
            val journalId = parentEntry.arguments?.getInt("journalId") ?: 0
            val photoUri = reviewEntry.arguments?.getString("photoUri")?.decodeUri()
            
            Scaffold { _ ->
                JournalEntryReview(
                    journalId = journalId,
                    captureUri = photoUri!!,
                    onCaptureSaved = {
                        rootNavController.popBackStack(
                            Route.JOURNAL_ENTRY_MAIN_SCREEN,
                            false,
                        )
                    },
                    onReCapture = { rootNavController.popBackStack() },
                )
            }
        }
    }
}
