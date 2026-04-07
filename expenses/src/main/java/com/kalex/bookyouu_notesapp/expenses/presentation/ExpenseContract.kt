package com.kalex.bookyouu_notesapp.expenses.presentation

import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.expenses.domain.model.Category
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import java.time.YearMonth

data class ExpenseUi(
    val id: Long,
    val amount: String,
    val description: String,
    val category: Category,
    val date: String
)

data class ExpenseState(
    val expenses: List<ExpenseUi> = emptyList(),
    val totalSpent: String = "$ 0.00",
    val selectedMonth: YearMonth = YearMonth.now(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface ExpenseAction {
    data class OnMonthChange(val newMonth: YearMonth) : ExpenseAction
    data class OnDeleteExpense(val id: Long) : ExpenseAction
    object OnAddExpenseClick : ExpenseAction
    data class OnSaveExpense(
        val amount: String,
        val description: String,
        val category: Category
    ) : ExpenseAction
}

sealed interface ExpenseEvent {
    object NavigateToAddExpense : ExpenseEvent
    data class ShowSnackbar(val message: UiText) : ExpenseEvent
    object ExpenseSaved : ExpenseEvent
}
