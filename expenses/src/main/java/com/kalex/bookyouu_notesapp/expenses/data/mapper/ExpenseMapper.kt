package com.kalex.bookyouu_notesapp.expenses.data.mapper

import com.kalex.bookyouu_notesapp.db.data.ExpenseEntity
import com.kalex.bookyouu_notesapp.expenses.domain.model.Category
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        amount = amount,
        description = description,
        category = Category.values().find { it.id == categoryId } ?: Category.OTHERS,
        date = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()),
        monthYear = monthYear
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = amount,
        description = description,
        categoryId = category.id,
        date = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        monthYear = monthYear
    )
}
