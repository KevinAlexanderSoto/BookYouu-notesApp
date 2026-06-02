package com.kalex.bookyouu_notesapp.investments.data.mapper

import com.kalex.bookyouu_notesapp.db.data.InvestmentEntity
import com.kalex.bookyouu_notesapp.db.data.InvestmentTransactionEntity
import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.model.InvestmentTransaction

fun InvestmentEntity.toDomain(): Investment {
    return Investment(
        id = id,
        name = name,
        type = type,
        initialAmount = initialAmount,
        currency = currency,
        dateCreated = dateCreated
    )
}

fun Investment.toEntity(): InvestmentEntity {
    return InvestmentEntity(
        id = id,
        name = name,
        type = type,
        initialAmount = initialAmount,
        currency = currency,
        dateCreated = dateCreated
    )
}

fun InvestmentTransactionEntity.toDomain(): InvestmentTransaction {
    return InvestmentTransaction(
        id = id,
        investmentId = investmentId,
        amount = amount,
        date = date,
        description = description,
        type = type
    )
}

fun InvestmentTransaction.toEntity(): InvestmentTransactionEntity {
    return InvestmentTransactionEntity(
        id = id,
        investmentId = investmentId,
        amount = amount,
        date = date,
        description = description,
        type = type
    )
}
