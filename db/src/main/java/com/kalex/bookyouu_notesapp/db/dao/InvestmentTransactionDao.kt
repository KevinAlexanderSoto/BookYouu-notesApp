package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kalex.bookyouu_notesapp.db.data.InvestmentTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentTransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: InvestmentTransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: InvestmentTransactionEntity)

    @Query("SELECT * FROM investment_transactions WHERE investment_id = :investmentId ORDER BY date DESC")
    fun getTransactionsForInvestment(investmentId: Long): Flow<List<InvestmentTransactionEntity>>

    @Query("SELECT * FROM investment_transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<InvestmentTransactionEntity>>
}
