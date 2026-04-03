package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObligationsScaffold(
    title: String,
    onNavigationClick: () -> Unit,
    onFloatingActionClick: () -> Unit,
    showNavigationIcon: Boolean = true,
    navigationIcon: ImageVector = Icons.Default.Menu,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006A6A) // Primary teal
                        )
                    )
                },
                navigationIcon = {
                    if (showNavigationIcon) {
                        IconButton(onClick = onNavigationClick) {
                            Icon(
                                imageVector = navigationIcon,
                                contentDescription = "navigation",
                                tint = Color(0xFF006A6A)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 8.dp),
                onClick = { onFloatingActionClick() },
            ) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}
