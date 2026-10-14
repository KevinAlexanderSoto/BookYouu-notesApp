package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.expenses.data.repository.FakeExpenseRepository
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class DeleteExpenseUseCaseTest {

    private lateinit var repository: FakeExpenseRepository
    private lateinit var deleteUseCase: DeleteExpenseUseCase
    private lateinit var addUseCase: AddExpenseUseCase

    private val baseDate = LocalDateTime.of(2026, 10, 14, 10, 0)

    @Before
    fun setUp() {
        repository = FakeExpenseRepository()
        deleteUseCase = DeleteExpenseUseCase(repository)
        addUseCase = AddExpenseUseCase(repository)
    }

    @Test
    fun `when invoke is called, delete single expense by id`() = runTest {
        val expense = Expense(
            amount = 15.0,
            description = "Lunch",
            category = Category.FOOD,
            date = baseDate,
            monthYear = "10-2026"
        )
        addUseCase(expense)
        val monthExpenses = repository.getExpensesByMonth("10-2026").first()
        assertEquals(1, monthExpenses.size)

        deleteUseCase(monthExpenses.first().id)
        val afterDelete = repository.getExpensesByMonth("10-2026").first()
        assertTrue(afterDelete.isEmpty())
    }

    @Test
    fun `when deleteExpenseSeries is called, delete all installments in series`() = runTest {
        val expense = Expense(
            amount = 300.0,
            description = "Phone",
            category = Category.SHOPPING,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = 3
        )
        addUseCase(expense)

        val series = repository.getExpenseSeries(1L)
        assertEquals(3, series.size)

        deleteUseCase.deleteExpenseSeries(1L)

        val seriesAfter = repository.getExpenseSeries(1L)
        assertTrue(seriesAfter.isEmpty())
    }

    @Test
    fun `when deleteFutureInstallments is called from installment 2, keep installment 1 and delete 2 and 3`() = runTest {
        val expense = Expense(
            amount = 300.0,
            description = "Tablet",
            category = Category.SHOPPING,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = 3
        )
        addUseCase(expense)

        val series = repository.getExpenseSeries(1L)
        assertEquals(3, series.size)

        deleteUseCase.deleteFutureInstallments(parentId = 1L, fromInstallment = 2)

        val seriesAfter = repository.getExpenseSeries(1L)
        assertEquals(1, seriesAfter.size)
        assertEquals(1, seriesAfter.first().currentInstallment)
    }
}
