package com.kalex.bookyouu_notesapp.payments.data.repository

import com.kalex.bookyouu_notesapp.db.dao.ObligationDao
import com.kalex.bookyouu_notesapp.payments.data.mapper.toDomain
import com.kalex.bookyouu_notesapp.payments.data.mapper.toEntity
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class RoomObligationRepository(
    private val dao: ObligationDao
) : ObligationRepository {

    override fun getObligations(): Flow<List<Obligation>> {
        return dao.getMonthlyObligations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun togglePaymentStatus(obligation: Obligation) {
        val newIsPaid = !obligation.isPaid
        val newLastPaidDate = if (newIsPaid) Date() else obligation.lastPaidDate
        
        val updatedObligation = obligation.copy(
            isPaid = newIsPaid,
            lastPaidDate = newLastPaidDate
        )
        dao.updateObligation(updatedObligation.toEntity())
    }

    override suspend fun addObligation(obligation: Obligation) {
        dao.insertObligation(obligation.toEntity())
    }
}
