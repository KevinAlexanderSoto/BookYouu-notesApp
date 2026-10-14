package com.kalex.bookyouu_notesapp.expenses.domain.repository

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpensesByMonth(monthYear: String): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense): Long
    suspend fun insertExpenses(expenses: List<Expense>): List<Long>
    suspend fun deleteExpense(id: Long)
    suspend fun deleteExpenseSeries(parentId: Long)
    suspend fun deleteFutureInstallments(parentId: Long, fromInstallment: Int)
    suspend fun getExpenseSeries(parentId: Long): List<Expense>
    suspend fun getExpenseById(id: Long): Expense?
}
