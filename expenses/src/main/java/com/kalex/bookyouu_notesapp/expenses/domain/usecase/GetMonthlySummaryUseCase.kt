package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.expenses.domain.model.Category
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.model.MonthlySummary
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMonthlySummaryUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(monthYear: String): Flow<MonthlySummary> {
        return repository.getExpensesByMonth(monthYear).map { expenses ->
            val totalSpent = expenses.sumOf { it.amount }
            val categoryPercentages = if (totalSpent > 0) {
                expenses.groupBy { it.category }
                    .mapValues { (_, items) -> (items.sumOf { it.amount } / totalSpent).toFloat() }
            } else {
                emptyMap()
            }
            MonthlySummary(expenses, totalSpent, categoryPercentages)
        }
    }
}
