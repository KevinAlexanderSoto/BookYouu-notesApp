package com.kalex.bookyouu_notesapp.payments.domain.repository

import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import kotlinx.coroutines.flow.Flow

interface ObligationRepository {
    fun getObligations(): Flow<List<Obligation>>
    suspend fun togglePaymentStatus(obligation: Obligation)
    suspend fun addObligation(obligation: Obligation): Int
    suspend fun deleteObligation(id: Int)
    suspend fun deleteObligations(ids: List<Int>)
}
