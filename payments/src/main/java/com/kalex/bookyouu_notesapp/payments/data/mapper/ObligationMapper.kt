package com.kalex.bookyouu_notesapp.payments.data.mapper

import com.kalex.bookyouu_notesapp.db.data.ObligationEntity
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation

fun ObligationEntity.toDomain(): Obligation {
    return Obligation(
        id = id,
        name = name,
        amount = amount,
        dayOfMonth = dayOfMonth,
        category = category,
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
        isPaid = isPaid,
        lastPaidDate = lastPaidDate
    )
}
