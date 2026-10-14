package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository

class DeleteExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteExpense(id)
    }

    suspend fun deleteExpenseSeries(parentId: Long) {
        repository.deleteExpenseSeries(parentId)
    }

    suspend fun deleteFutureInstallments(parentId: Long, fromInstallment: Int) {
        repository.deleteFutureInstallments(parentId, fromInstallment)
    }
}
