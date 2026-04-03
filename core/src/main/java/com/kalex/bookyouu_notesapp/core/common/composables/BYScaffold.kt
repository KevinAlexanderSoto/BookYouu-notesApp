package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.core.common.BottomBarNavigationItem


@Composable
fun ScaffoldBottomBar(
    currentDestination: String,
    onBottomNavigationClick: (String) -> Unit,
    bottomNavigationBarScreens: List<BottomBarNavigationItem>,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val bottomNavigationScreens = remember { bottomNavigationBarScreens }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTopBar(
    title: String? = null,
    onBackNavigationClick: () -> Unit,
    showNavigationIcon: Boolean = true,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                navigationIcon = {
                    if (showNavigationIcon) {
                        IconButton(onClick = onBackNavigationClick) {
                            Icon(
                                imageVector = navigationIcon,
                                contentDescription = "navigation Icon",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldFloatingButtonAndTopBar(
    title: String? = null,
    onBackNavigationClick: () -> Unit,
    onFloatingActionClick: () -> Unit,
    showNavigationIcon: Boolean = true,
    navigationIcon: ImageVector = Icons.Default.ArrowBack,
    content: @Composable (padding: PaddingValues) -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                navigationIcon = {
                    if (showNavigationIcon) {
                        IconButton(onClick = onBackNavigationClick) {
                            Icon(
                                imageVector = navigationIcon,
                                contentDescription = "navigation Icon",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
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
