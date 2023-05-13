package com.kalex.bookyouu_notesapp.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kalex.bookyouu_notesapp.R

sealed class BottomNavigationScreens(
    val route: String,
    @DrawableRes val bottomIconRes: Int,
    @StringRes val label: Int,
) {
    object Subject : BottomNavigationScreens(
        Route.SUBJECT_LIST,
        R.drawable.ic_launcher_background,
        androidx.compose.ui.R.string.navigation_menu,
    )

    object Average : BottomNavigationScreens(
        Route.AVERAGE_MAIN,
        R.drawable.ic_launcher_background,
        androidx.compose.ui.R.string.navigation_menu,
    )

    companion object {
        val bottomNavItems = listOf(
            Subject,
            Average,
        )
    }
}
