package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository

class GetExpenseByIdUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(id: Long): Expense? {
        return repository.getExpenseById(id)
    }
}
