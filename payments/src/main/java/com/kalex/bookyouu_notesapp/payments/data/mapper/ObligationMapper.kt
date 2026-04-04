package com.kalex.bookyouu_notesapp.payments.data.mapper

import com.kalex.bookyouu_notesapp.db.data.ObligationEntity
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.model.PaymentFrequency

fun ObligationEntity.toDomain(): Obligation {
    return Obligation(
        id = id,
        name = name,
        amount = amount,
        dayOfMonth = dayOfMonth,
        category = category,
        frequency = PaymentFrequency.valueOf(frequency),
        isPaid = isPaid,
        lastPaidDate = lastPaidDate
    )
}

fun Obligation.toEntity(): ObligationEntity {
    return ObligationEntity(
        id = id,
        name = name,
        amount = amount,
        dayOfMonth = dayOfMonth,
        category = category,
        frequency = frequency.name,
        isPaid = isPaid,
        lastPaidDate = lastPaidDate
    )
}
