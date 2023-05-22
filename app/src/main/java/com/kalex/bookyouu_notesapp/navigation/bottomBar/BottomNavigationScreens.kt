package com.kalex.bookyouu_notesapp.navigation.bottomBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.navigation.Route

sealed class BottomNavigationScreens(
    val route: String,
    @DrawableRes val bottomIconRes: Int,
    @StringRes val label: Int,
) {
    object Subject : BottomNavigationScreens(
        Route.SUBJECT_LIST,
        R.drawable.book_svgrepo_com,
        R.string.subject_bottom_label,
    )

    object Average : BottomNavigationScreens(
        Route.AVERAGE_MAIN,
        R.drawable.archive_svgrepo_com,
        R.string.average_bottom_label,
    )

    companion object {
        val bottomNavItems = listOf(
            Subject,
            Average,
        )
    }
}
