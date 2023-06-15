package com.kalex.bookyouu_notesapp.navigation.bottomBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    onNavigationClick: (String) -> Unit,
    bottomNavigationItems: List<BottomNavigationScreens>,
    currentDestination: String,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(73.dp)
            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)),

    ) {
        bottomNavigationItems.forEach { item ->
            val selected = currentDestination == item.route
            NavigationBarItem(
                alwaysShowLabel = false,
                selected = selected,
                onClick = { onNavigationClick(item.route) },
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
}
