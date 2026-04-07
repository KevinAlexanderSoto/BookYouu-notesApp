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
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpense(id: Long)

    @Query("SELECT * FROM expenses WHERE month_year = :monthYear ORDER BY date DESC")
    fun getExpensesByMonth(monthYear: String): Flow<List<ExpenseEntity>>
}
