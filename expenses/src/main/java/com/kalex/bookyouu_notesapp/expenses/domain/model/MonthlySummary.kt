package com.kalex.bookyouu_notesapp.expenses.domain.model

import com.kalex.bookyouu_notesapp.core.common.Category

data class MonthlySummary(
    val expenses: List<Expense>,
    val totalSpent: Double,
    val categoryPercentages: Map<Category, Float>
)
