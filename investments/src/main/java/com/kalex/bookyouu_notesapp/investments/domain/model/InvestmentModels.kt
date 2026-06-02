package com.kalex.bookyouu_notesapp.investments.domain.model

data class Investment(
    val id: Long = 0,
    val name: String,
    val type: String,
    val initialAmount: Double,
    val currency: String = "USD",
    val dateCreated: Long
)

data class InvestmentTransaction(
    val id: Long = 0,
    val investmentId: Long,
    val amount: Double,
    val date: Long,
    val description: String,
    val type: String
)
