package com.kalex.bookyouu_notesapp.payments.domain.usecase

import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository

class TogglePaymentUseCase(
    private val repository: ObligationRepository
) {
    suspend operator fun invoke(obligation: Obligation) {
        repository.togglePaymentStatus(obligation)
    }
}
