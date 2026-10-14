package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.repository.ExpenseRepository
import java.math.BigDecimal
import java.math.RoundingMode

class AddExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        if (expense.amount <= 0) {
            throw IllegalArgumentException("Amount must be greater than zero")
        }

        val totalInstallments = expense.totalInstallments
        if (totalInstallments == null || totalInstallments <= 1) {
            repository.insertExpense(
                expense.copy(
                    totalInstallments = null,
                    currentInstallment = null,
                    parentExpenseId = null
                )
            )
            return
        }

        // If editing an existing installment series, clean up existing series first
        val existingParentId = expense.parentExpenseId ?: if (expense.id != 0L && expense.currentInstallment != null) expense.id else null
        if (existingParentId != null) {
            repository.deleteExpenseSeries(existingParentId)
        }

        val totalBd = BigDecimal.valueOf(expense.amount).setScale(2, RoundingMode.HALF_UP)
        val nBd = BigDecimal.valueOf(totalInstallments.toLong())
        val baseInstallment = totalBd.divide(nBd, 2, RoundingMode.FLOOR)
        val lastInstallment = totalBd.subtract(baseInstallment.multiply(BigDecimal.valueOf((totalInstallments - 1).toLong())))

        val baseDate = expense.date

        val firstExpense = expense.copy(
            id = 0L,
            amount = baseInstallment.toDouble(),
            totalInstallments = totalInstallments,
            currentInstallment = 1,
            parentExpenseId = null,
            date = baseDate,
            monthYear = "${baseDate.monthValue.toString().padStart(2, '0')}-${baseDate.year}"
        )
        val parentId = repository.insertExpense(firstExpense)

        if (totalInstallments > 1) {
            val subsequentInstallments = (2..totalInstallments).map { k ->
                val installmentDate = baseDate.plusMonths((k - 1).toLong())
                val monthYearStr = "${installmentDate.monthValue.toString().padStart(2, '0')}-${installmentDate.year}"
                val amount = if (k == totalInstallments) lastInstallment.toDouble() else baseInstallment.toDouble()
                expense.copy(
                    id = 0L,
                    amount = amount,
                    totalInstallments = totalInstallments,
                    currentInstallment = k,
                    parentExpenseId = parentId,
                    date = installmentDate,
                    monthYear = monthYearStr
                )
            }
            repository.insertExpenses(subsequentInstallments)
        }
    }
}
