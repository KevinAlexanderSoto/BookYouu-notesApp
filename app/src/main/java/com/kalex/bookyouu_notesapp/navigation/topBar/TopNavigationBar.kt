package com.kalex.bookyouu_notesapp.navigation.topBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(onBackNavigation: () -> Unit, topBarTitle: String) {
    TopAppBar(
        title = { Text(text = topBarTitle) },
        navigationIcon = {
            if (topBarTitle !== "") {
                IconButton(
                    onClick = { onBackNavigation() },
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
