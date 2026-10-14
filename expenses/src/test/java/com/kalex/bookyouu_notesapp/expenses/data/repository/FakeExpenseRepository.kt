package com.kalex.bookyouu_notesapp.expenses.data.repository

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeExpenseRepository : ExpenseRepository {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    private var nextId = 1L

    override fun getExpensesByMonth(monthYear: String): Flow<List<Expense>> {
        return _expenses.map { expenses ->
            expenses.filter { it.monthYear == monthYear }
        }
    }

    override suspend fun insertExpense(expense: Expense): Long {
        val current = _expenses.value.toMutableList()
        val index = current.indexOfFirst { it.id == expense.id && expense.id != 0L }
        val idToReturn: Long
        if (index != -1) {
            current[index] = expense
            idToReturn = expense.id
        } else {
            val newId = if (expense.id == 0L) nextId++ else expense.id
            current.add(expense.copy(id = newId))
            idToReturn = newId
        }
        _expenses.value = current
        return idToReturn
    }

    override suspend fun insertExpenses(expenses: List<Expense>): List<Long> {
        return expenses.map { insertExpense(it) }
    }

    override suspend fun deleteExpense(id: Long) {
        _expenses.value = _expenses.value.filter { it.id != id }
    }

    override suspend fun deleteExpenseSeries(parentId: Long) {
        _expenses.value = _expenses.value.filter { it.parentExpenseId != parentId && it.id != parentId }
    }

    override suspend fun deleteFutureInstallments(parentId: Long, fromInstallment: Int) {
        _expenses.value = _expenses.value.filterNot {
            (it.parentExpenseId == parentId || it.id == parentId) && (it.currentInstallment ?: 1) >= fromInstallment
        }
    }

    override suspend fun getExpenseSeries(parentId: Long): List<Expense> {
        return _expenses.value.filter { it.parentExpenseId == parentId || it.id == parentId }
            .sortedBy { it.currentInstallment ?: 1 }
    }

    override suspend fun getExpenseById(id: Long): Expense? {
        return _expenses.value.find { it.id == id }
    }
}
