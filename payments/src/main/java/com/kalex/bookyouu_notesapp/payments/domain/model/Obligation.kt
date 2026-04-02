package com.kalex.bookyouu_notesapp.payments.domain.model

import java.util.Date

data class Obligation(
    val id: Int = 0,
    val name: String,
    val amount: Double,
    val dayOfMonth: Int,
    val category: String,
    val isPaid: Boolean = false,
    val lastPaidDate: Date? = null
)
