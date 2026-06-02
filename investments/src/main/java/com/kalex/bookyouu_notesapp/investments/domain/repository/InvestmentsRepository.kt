package com.kalex.bookyouu_notesapp.investments.domain.repository

import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.model.InvestmentTransaction
import kotlinx.coroutines.flow.Flow

interface InvestmentsRepository {
    fun getInvestments(): Flow<List<Investment>>
    fun getInvestmentById(id: Long): Flow<Investment>
    suspend fun upsertInvestment(investment: Investment)
    suspend fun deleteInvestment(investment: Investment)
    
    fun getTransactionsForInvestment(investmentId: Long): Flow<List<InvestmentTransaction>>
    suspend fun upsertTransaction(transaction: InvestmentTransaction)
    suspend fun deleteTransaction(transaction: InvestmentTransaction)
}
