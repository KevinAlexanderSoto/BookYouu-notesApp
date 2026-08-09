package com.kalex.bookyouu_notesapp.investments.domain.repository

import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import kotlinx.coroutines.flow.Flow

interface InvestmentsRepository {
    fun getInvestments(): Flow<List<Investment>>
    fun getInvestmentById(id: Long): Flow<Investment>
    suspend fun upsertInvestment(investment: Investment)
    suspend fun deleteInvestment(investment: Investment)

}
