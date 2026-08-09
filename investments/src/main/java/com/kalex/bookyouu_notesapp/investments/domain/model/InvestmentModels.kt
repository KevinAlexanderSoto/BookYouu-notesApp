package com.kalex.bookyouu_notesapp.investments.domain.model

import com.kalex.bookyouu_notesapp.investments.presentation.InvestmentType

data class Investment(
    val id: Long = 0,
    val name: String,
    val type: InvestmentType,
    val initialAmount: Double,
    val currency: String = "USD",
    val dateCreated: Long
)