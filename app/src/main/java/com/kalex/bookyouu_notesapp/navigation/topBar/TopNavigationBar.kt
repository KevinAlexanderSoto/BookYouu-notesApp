package com.kalex.bookyouu_notesapp.navigation.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    AnimatedVisibility(visible = !BottomNavigationScreens.bottomNavItems.any { it.route == currentDestination?.route }) {
        val topBarTitle = TopBarTitleFactory().getTopBarTitle(currentDestination?.route)
        TopAppBar(
            title = { Text(text = topBarTitle) },
            navigationIcon = {
                if (topBarTitle == "") {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back",
                        )
                    }
                }
            },
        )
    }
}
