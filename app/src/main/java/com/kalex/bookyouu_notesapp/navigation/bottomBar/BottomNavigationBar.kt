package com.kalex.bookyouu_notesapp.navigation.bottomBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (BottomNavigationScreens.bottomNavItems.any { it.route == currentDestination?.route }) {
        NavigationBar(
            modifier = Modifier.height(70.dp).fillMaxWidth().clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        ) {
            BottomNavigationScreens.bottomNavItems.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route) },
                    label = { Text(text = stringResource(item.label)) },
                    icon = {
                        Icon(
                            painterResource(id = item.bottomIconRes),
                            contentDescription = "",
                        )
                    },
                )
            }
        }
    }
}
