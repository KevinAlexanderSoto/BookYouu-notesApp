package com.kalex.bookyouu_notesapp.expenses.data.repository

import com.kalex.bookyouu_notesapp.db.dao.ExpenseDao
import com.kalex.bookyouu_notesapp.expenses.data.mapper.toDomain
import com.kalex.bookyouu_notesapp.expenses.data.mapper.toEntity
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomExpenseRepository(
    private val dao: ExpenseDao
) : ExpenseRepository {
    override fun getExpensesByMonth(monthYear: String): Flow<List<Expense>> {
        return dao.getExpensesByMonth(monthYear).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertExpense(expense: Expense) {
        dao.insertExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(id: Long) {
        dao.deleteExpense(id)
    }
}
