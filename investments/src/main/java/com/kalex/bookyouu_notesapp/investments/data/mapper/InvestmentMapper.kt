package com.kalex.bookyouu_notesapp.investments.data.mapper

import com.kalex.bookyouu_notesapp.db.data.InvestmentEntity
import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.presentation.Currency
import com.kalex.bookyouu_notesapp.investments.presentation.InvestmentType

fun InvestmentEntity.toDomain(): Investment {
    val safeType = runCatching { InvestmentType.valueOf(type) }
        .getOrDefault(InvestmentType.GENERAL)
    val safeCurrency = runCatching { Currency.valueOf(currency) }
        .getOrDefault(Currency.USD)
    return Investment(
        id = id,
        name = name,
        type = safeType,
        initialAmount = initialAmount,
        currency = safeCurrency,
        dateCreated = dateCreated
    )
}

fun Investment.toEntity(): InvestmentEntity {
    return InvestmentEntity(
        id = id,
        name = name,
        type = type.name,
        initialAmount = initialAmount,
        currency = currency.code,
        dateCreated = dateCreated
    )
}
