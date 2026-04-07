package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
