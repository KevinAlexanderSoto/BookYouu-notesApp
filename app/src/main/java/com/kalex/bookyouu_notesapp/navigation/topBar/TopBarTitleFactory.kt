package com.kalex.bookyouu_notesapp.navigation.topBar

import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.navigation.Route

object TopBarTitleFactory {

    /**
     * Gets the string resource ID for the top bar title based on the provided route.
     *
     * @param route The current navigation route string.
     * @return The resource ID of the top bar title string, or a default empty title if the route is unknown.
     */
    fun getTopBarTitle(route: String?) =
        when (route) {
            Route.SUBJECT_FORM -> {
                R.string.subject_form_topBar_title
            }
            Route.RECORDS_MAIN_SCREEN -> {
                R.string.record_mainScreen_topBar_title
            }
            Route.RECORDS_DETAIL_PARAM_SCREEN -> {
                R.string.record_detailScreen_topBar_title
            }
            Route.PAYMENTS_MAIN -> {
                R.string.obligations_topBar_title
            }
            Route.PAYMENTS_CREATE -> {
                R.string.obligations_create_topBar_title
            }
            else -> { R.string.empty_topBar_title }
        }
}
