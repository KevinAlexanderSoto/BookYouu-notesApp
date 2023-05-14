package com.kalex.bookyouu_notesapp.navigation.topBar

import com.kalex.bookyouu_notesapp.navigation.Route

class TopBarTitleFactory {

    fun getTopBarTitle(route: String?) =
        when (route) {
            Route.SUBJECT_FORM -> {
                "Crear Subject"
            }

            else -> {
                " "
            }
        }
}
