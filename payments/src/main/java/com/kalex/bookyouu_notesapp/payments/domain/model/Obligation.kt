package com.kalex.bookyouu_notesapp.payments.domain.model

import java.util.Date

enum class PaymentFrequency {
    MONTHLY,
    WEEKLY,
    BI_WEEKLY
}

enum class ObligationCategory {
    HOUSE,
    SUBSCRIPTION,
    GYM,
    UTILITY,
    GENERAL,
    TRANSPORT
}

data class Obligation(
    val id: Int = 0,
    val name: String,
    val amount: Double,
    val dayOfMonth: Int,
    val category: String,
    val frequency: PaymentFrequency = PaymentFrequency.MONTHLY,
    val isPaid: Boolean = false,
    val lastPaidDate: Date? = null
)
