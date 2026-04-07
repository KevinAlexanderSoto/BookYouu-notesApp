package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetMonthlyExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(monthYear: String): Flow<List<Expense>> {
        return repository.getExpensesByMonth(monthYear)
    }
}
