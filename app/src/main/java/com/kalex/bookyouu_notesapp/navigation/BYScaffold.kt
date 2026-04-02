package com.kalex.bookyouu_notesapp.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BYNavigationBar
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BYNavigationBarItem
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens
import com.kalex.bookyouu_notesapp.navigation.topBar.TopBarTitleFactory
import com.kalex.bookyouu_notesapp.navigation.topBar.TopNavigationBar

@Composable
fun ScaffoldBottomBar(
    currentDestination: String,
    onBottomNavigationClick: (String) -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val bottomNavigationScreens = remember { BottomNavigationScreens.bottomNavItems }
    Scaffold(
        bottomBar = {
            BYNavigationBar(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                bottomNavigationScreens.forEach { item ->
                    val selected = currentDestination == item.route
                    BYNavigationBarItem(
                        selected = selected,
                        onClick = { onBottomNavigationClick(item.route) },
                        label = item.label,
                        icon = item.bottomIconRes
                    )
                }
            }
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
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 14.dp),
                onClick = { onFloatingActionClick() },
            ) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}
