package com.kalex.bookyouu_notesapp.expenses.domain.repository

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpensesByMonth(monthYear: String): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(id: Long)
}
