package com.kalex.bookyouu_notesapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
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
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(73.dp)
                    .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)),
            ) {
                bottomNavigationScreens.forEach { item ->
                    val selected = currentDestination == item.route
                    NavigationBarItem(
                        alwaysShowLabel = false,
                        selected = selected,
                        onClick = { onBottomNavigationClick(item.route) },
                        label = { Text(text = stringResource(item.label)) },
                        icon = {
                            Icon(
                                painterResource(id = item.bottomIconRes),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                            )
                        },
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
