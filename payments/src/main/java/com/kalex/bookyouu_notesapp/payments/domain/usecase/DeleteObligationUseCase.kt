package com.kalex.bookyouu_notesapp.payments.domain.usecase

import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository

class DeleteObligationUseCase(
    private val repository: ObligationRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteObligation(id)
    }

    suspend fun deleteMultiple(ids: List<Int>) {
        repository.deleteObligations(ids)
    }
}
