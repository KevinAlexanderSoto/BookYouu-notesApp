package com.kalex.bookyouu_notesapp.expenses.data.mapper

import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.db.data.ExpenseEntity
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        amount = amount,
        description = description,
        category = Category.fromId(categoryId),
        date = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()),
        monthYear = monthYear,
        totalInstallments = totalInstallments
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = amount,
        description = description,
        categoryId = category.id,
        date = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        monthYear = monthYear,
        totalInstallments = totalInstallments
    )
}
