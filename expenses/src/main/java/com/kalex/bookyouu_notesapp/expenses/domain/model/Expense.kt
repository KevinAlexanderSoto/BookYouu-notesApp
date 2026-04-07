package com.kalex.bookyouu_notesapp.expenses.domain.model

import java.time.LocalDateTime

data class Expense(
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val category: Category,
    val date: LocalDateTime,
    val monthYear: String // Format: "MM-YYYY"
)
