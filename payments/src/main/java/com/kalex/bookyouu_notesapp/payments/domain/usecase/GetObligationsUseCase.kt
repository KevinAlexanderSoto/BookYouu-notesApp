package com.kalex.bookyouu_notesapp.payments.domain.usecase

import com.kalex.bookyouu_notesapp.payments.domain.model.PaymentsSummary
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetObligationsUseCase(
    private val repository: ObligationRepository
) {
    operator fun invoke(): Flow<PaymentsSummary> {
        return repository.getObligations().map { list ->
            PaymentsSummary(
                obligations = list,
                totalBalance = list.sumOf { it.amount },
                pendingAmount = list.filter { !it.isPaid }.sumOf { it.amount },
                paidAmount = list.filter { it.isPaid }.sumOf { it.amount }
            )
        }
    }
}
