package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kalex.bookyouu_notesapp.db.data.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>): List<Long>

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpense(id: Long)

    @Query("DELETE FROM expenses WHERE parent_expense_id = :parentId OR id = :parentId")
    suspend fun deleteExpenseSeries(parentId: Long)

    @Query("DELETE FROM expenses WHERE (parent_expense_id = :parentId OR id = :parentId) AND current_installment >= :fromInstallment")
    suspend fun deleteFutureInstallments(parentId: Long, fromInstallment: Int)

    @Query("SELECT * FROM expenses WHERE parent_expense_id = :parentId OR id = :parentId ORDER BY current_installment ASC")
    suspend fun getExpenseSeries(parentId: Long): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Query("SELECT * FROM expenses WHERE month_year = :monthYear ORDER BY date DESC")
    fun getExpensesByMonth(monthYear: String): Flow<List<ExpenseEntity>>
}
