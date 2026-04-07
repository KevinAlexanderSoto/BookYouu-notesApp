package com.kalex.bookyouu_notesapp.expenses.domain.model

data class MonthlySummary(
    val expenses: List<Expense>,
    val totalSpent: Double,
    val categoryPercentages: Map<Category, Float>
)
