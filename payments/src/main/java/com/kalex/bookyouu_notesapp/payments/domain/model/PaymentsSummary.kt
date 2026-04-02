package com.kalex.bookyouu_notesapp.payments.domain.model

data class PaymentsSummary(
    val totalBalance: Double = 0.0,
    val pendingAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val obligations: List<Obligation> = emptyList()
)
