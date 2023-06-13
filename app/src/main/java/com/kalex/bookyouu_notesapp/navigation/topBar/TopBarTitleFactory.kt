package com.kalex.bookyouu_notesapp.navigation.topBar

import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.navigation.Route

class TopBarTitleFactory {

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
            else -> { R.string.empty_topBar_title }
        }
}
