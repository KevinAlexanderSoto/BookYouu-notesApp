package com.kalex.bookyouu_notesapp.expenses.data.repository

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeExpenseRepository : ExpenseRepository {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())

    override fun getExpensesByMonth(monthYear: String): Flow<List<Expense>> {
        return _expenses.map { expenses ->
            expenses.filter { it.monthYear == monthYear }
        }
    }

    override suspend fun insertExpense(expense: Expense) {
        val current = _expenses.value.toMutableList()
        val index = current.indexOfFirst { it.id == expense.id && expense.id != 0L }
        if (index != -1) {
            current[index] = expense
        } else {
            val newId = if (expense.id == 0L) (current.size + 1).toLong() else expense.id
            current.add(expense.copy(id = newId))
        }
        _expenses.value = current
    }

    override suspend fun deleteExpense(id: Long) {
        _expenses.value = _expenses.value.filter { it.id != id }
    }
}
