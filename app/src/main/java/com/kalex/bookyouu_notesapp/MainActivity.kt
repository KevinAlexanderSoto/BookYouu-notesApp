package com.kalex.bookyouu_notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kalex.bookyouu_notesapp.common.theme.BookYouUnotesAppTheme
import com.kalex.bookyouu_notesapp.navigation.Route
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationBar
import com.kalex.bookyouu_notesapp.navigation.bottomBar.BottomNavigationScreens
import com.kalex.bookyouu_notesapp.navigation.graphs.RootNavigationGraph
import com.kalex.bookyouu_notesapp.navigation.topBar.TopBarTitleFactory
import com.kalex.bookyouu_notesapp.navigation.topBar.TopNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topBarTitle = TopBarTitleFactory()
        val bottomNavigationScreens = BottomNavigationScreens.bottomNavItems
        setContent {
            BookYouUnotesAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(bottomNavigationScreens.any { it.route == currentDestination?.route }) {
                            BottomNavigationBar(
                                navController,
                                bottomNavigationScreens,
                                currentDestination,
                            )
                        }
                    },
                    topBar = {
                        AnimatedVisibility(!bottomNavigationScreens.any { it.route == currentDestination?.route }) {
                            TopNavigationBar(
                                navController,
                                topBarTitle.getTopBarTitle(currentDestination?.route),
                            )
                        }
                    },
                ) { paddingValues ->
                    RootNavigationGraph(
                        navController,
                        Modifier.padding(paddingValues).consumeWindowInsets(paddingValues),
                        startDestination = Route.SUBJECT,
                    )
                }
            }
        }
    }

}
