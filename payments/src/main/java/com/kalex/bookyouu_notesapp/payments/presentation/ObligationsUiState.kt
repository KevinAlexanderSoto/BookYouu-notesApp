package com.kalex.bookyouu_notesapp.payments.presentation

import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation

data class ObligationsUiState(
    val totalBalance: Double = 0.0,
    val pendingAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val obligations: List<Obligation> = emptyList(),
    val isLoading: Boolean = false
)
