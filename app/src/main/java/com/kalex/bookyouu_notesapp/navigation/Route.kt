package com.kalex.bookyouu_notesapp.navigation

object Route {
    const val AUTHENTICATION_ROUTE = "authentication_graph"
    const val AUTHENTICATION_SCREEN = "authentication_main_screen"
    const val SUBJECT = "subject_graph"
    const val SUBJECT_LIST = "subject_list"
    const val SUBJECT_FORM = "subject_form"
    const val SUBJECT_FORM_SUCCESS_SCREEN = "subject_form_success_screen"
    const val SUBJECT_DETAIL = "subject_detail"

    const val AVERAGE = "average_graph"
    const val AVERAGE_MAIN = "average_main"

    const val RECORDS_PARAM_ROUTE = "record_graph/{subjectID}"
    const val RECORDS_MAIN_ROUTE = "record_graph"
    const val RECORDS_MAIN_SCREEN = "record_main"
    const val RECORDS_CAPTURE = "record_capture"
    const val RECORDS_PARAM_REVIEW = "record_review/{photoUri}"
    const val RECORDS_MAIN_REVIEW = "record_review"
    const val RECORDS_DETAIL_MAIN_ROUTE = "record_details"
    const val RECORDS_DETAIL_PARAM_SCREEN = "record_details/{noteID}"

    const val MORE_MENU = "more_menu_graph"
    const val MORE_MENU_MAIN = "more_menu_main"
}