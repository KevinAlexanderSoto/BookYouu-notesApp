package com.kalex.bookyouu_notesapp.payments.domain.usecase

import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPendingObligationUseCase(
    private val repository: ObligationRepository
) {
    operator fun invoke(): Flow<List<Obligation>> {
        return repository.getObligations()
            .map { list ->
                list.filter { !it.isPaid }
            }
    }
}
