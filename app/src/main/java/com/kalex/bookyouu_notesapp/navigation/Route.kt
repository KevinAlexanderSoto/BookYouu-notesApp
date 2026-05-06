package com.kalex.bookyouu_notesapp.navigation

object Route {
    const val AUTHENTICATION_ROUTE = "authentication_graph"
    const val AUTHENTICATION_SCREEN = "authentication_main_screen"

    const val JOURNAL = "journal_graph"
    const val JOURNAL_LIST = "journal_list"
    const val JOURNAL_FORM = "journal_form"
    const val JOURNAL_FORM_SUCCESS_SCREEN = "journal_form_success_screen"
    const val JOURNAL_DETAIL = "journal_detail"

    const val JOURNAL_ENTRY_PARAM_ROUTE = "journal_entry_graph/{journalId}"
    const val JOURNAL_ENTRY_MAIN_ROUTE = "journal_entry_graph"
    const val JOURNAL_ENTRY_MAIN_SCREEN = "journal_entry_main"
    const val JOURNAL_ENTRY_CAPTURE = "journal_entry_capture"
    const val JOURNAL_ENTRY_PARAM_REVIEW = "journal_entry_review/{photoUri}"
    const val JOURNAL_ENTRY_MAIN_REVIEW = "journal_entry_review"
    const val JOURNAL_ENTRY_DETAIL_MAIN_ROUTE = "journal_entry_details"
    const val JOURNAL_ENTRY_DETAIL_PARAM_SCREEN = "journal_entry_details/{entryId}"

    const val MORE_MENU = "more_menu_graph"
    const val MORE_MENU_MAIN = "more_menu_main"

    const val PAYMENTS = "payments_graph"
    const val PAYMENTS_MAIN = "payments_main"
    const val PAYMENTS_CREATE: String = "payments_create?obligationId={obligationId}"

    const val EXPENSES = "expenses_graph"
    const val EXPENSES_LIST = "expenses_list"
    const val ADD_EXPENSE = "add_expense?expenseId={expenseId}"
}
