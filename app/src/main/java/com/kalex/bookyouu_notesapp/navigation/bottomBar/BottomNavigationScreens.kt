package com.kalex.bookyouu_notesapp.navigation.bottomBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.core.common.BottomBarNavigationItem
import com.kalex.bookyouu_notesapp.navigation.Route

sealed class BottomNavigationScreens(
    override val route: String,
    @DrawableRes override val bottomIconRes: Int,
    @StringRes override val label: Int,
): BottomBarNavigationItem {
    object Subject : BottomNavigationScreens(
        Route.SUBJECT_LIST,
        R.drawable.book_svgrepo_com,
        R.string.subject_bottom_label,
    )

    object Payments : BottomNavigationScreens(
        Route.PAYMENTS_MAIN,
        R.drawable.baseline_library_books_24,
        R.string.payments_bottom_label,
    )
    object Expenses : BottomNavigationScreens(
        Route.EXPENSES_LIST,
        R.drawable.baseline_calculate_24,
        R.string.expenses_bottom_label,
    )
    object MoreMenu : BottomNavigationScreens(
        Route.MORE_MENU_MAIN,
        R.drawable.settings_24px,
        R.string.moreMenu_bottom_label,
    )

    companion object {
        val bottomNavItems = listOf(
            Payments,
            Expenses,
            Subject,
            MoreMenu,
        )
    }
}
