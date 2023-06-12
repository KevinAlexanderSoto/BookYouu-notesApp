package com.kalex.bookyouu_notesapp.common.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationBar
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens
import com.kalex.bookyouu_notesapp.navigation.topBar.TopBarTitleFactory
import com.kalex.bookyouu_notesapp.navigation.topBar.TopNavigationBar

@Composable
fun ScaffoldBottomBar(
    currentDestination: NavDestination?,
    onBottomNavigationClick: (String) -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val bottomNavigationScreens = remember { BottomNavigationScreens.bottomNavItems }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onNavigationClick = {
                    onBottomNavigationClick(it)
                },
                bottomNavigationScreens,
                currentDestination,
            )
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun ScaffoldTopBar(
    currentDestination: NavDestination?,
    onBackNavigationClick: () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val topBarTitleFactory = remember { TopBarTitleFactory() }
    Scaffold(
        topBar = {
            TopNavigationBar(
                onBackNavigation = {
                    onBackNavigationClick()
                },
                topBarTitle = topBarTitleFactory.getTopBarTitle(currentDestination?.route),
            )
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun ScaffoldFloatingButtonAndTopBar(
    currentDestination: NavDestination?,
    onBackNavigationClick: () -> Unit,
    onFloatingActionClick: () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val topBarTitleFactory = remember { TopBarTitleFactory() }
    Scaffold(
        topBar = {
            TopNavigationBar(
                onBackNavigation = {
                    onBackNavigationClick()
                },
                topBarTitle = topBarTitleFactory.getTopBarTitle(currentDestination?.route),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onFloatingActionClick() }) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}
