package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.expenses.data.repository.FakeExpenseRepository
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class AddExpenseUseCaseTest {

    private lateinit var repository: FakeExpenseRepository
    private lateinit var useCase: AddExpenseUseCase

    private val baseDate = LocalDateTime.of(2026, 10, 14, 10, 0)

    @Before
    fun setUp() {
        repository = FakeExpenseRepository()
        useCase = AddExpenseUseCase(repository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when amount is zero or negative, throw IllegalArgumentException`() = runTest {
        val expense = Expense(
            amount = 0.0,
            description = "Test",
            category = Category.FOOD,
            date = baseDate,
            monthYear = "10-2026"
        )
        useCase(expense)
    }

    @Test
    fun `when totalInstallments is null or 1, save single expense`() = runTest {
        val expense = Expense(
            amount = 50.0,
            description = "Groceries",
            category = Category.FOOD,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = null
        )
        useCase(expense)

        val expenses = repository.getExpensesByMonth("10-2026").first()
        assertEquals(1, expenses.size)
        assertEquals(50.0, expenses.first().amount, 0.001)
        assertNull(expenses.first().currentInstallment)
        assertNull(expenses.first().totalInstallments)
    }

    @Test
    fun `when totalInstallments is 3 and amount is 100, calculate and schedule across 3 months with remainder absorbed`() = runTest {
        val expense = Expense(
            amount = 100.0,
            description = "Course",
            category = Category.EDUCATION,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = 3
        )
        useCase(expense)

        val month1 = repository.getExpensesByMonth("10-2026").first()
        val month2 = repository.getExpensesByMonth("11-2026").first()
        val month3 = repository.getExpensesByMonth("12-2026").first()

        assertEquals(1, month1.size)
        assertEquals(1, month2.size)
        assertEquals(1, month3.size)

        val first = month1.first()
        val second = month2.first()
        val third = month3.first()

        // Check amounts
        assertEquals(33.33, first.amount, 0.001)
        assertEquals(33.33, second.amount, 0.001)
        assertEquals(33.34, third.amount, 0.001) // Absorbs remainder cent

        // Total matches exact original amount
        assertEquals(100.0, first.amount + second.amount + third.amount, 0.001)

        // Check installment indices
        assertEquals(1, first.currentInstallment)
        assertEquals(3, first.totalInstallments)
        assertNull(first.parentExpenseId)

        assertEquals(2, second.currentInstallment)
        assertEquals(3, second.totalInstallments)
        assertEquals(first.id, second.parentExpenseId)

        assertEquals(3, third.currentInstallment)
        assertEquals(3, third.totalInstallments)
        assertEquals(first.id, third.parentExpenseId)

        // Check dates
        assertEquals(10, first.date.monthValue)
        assertEquals(11, second.date.monthValue)
        assertEquals(12, third.date.monthValue)
    }

    @Test
    fun `when totalInstallments is 12 and amount is 1200, schedule 12 installments across 12 months`() = runTest {
        val expense = Expense(
            amount = 1200.0,
            description = "Laptop",
            category = Category.SHOPPING,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = 12
        )
        useCase(expense)

        var totalSum = 0.0
        for (i in 0 until 12) {
            val date = baseDate.plusMonths(i.toLong())
            val monthYearStr = "${date.monthValue.toString().padStart(2, '0')}-${date.year}"
            val monthExpenses = repository.getExpensesByMonth(monthYearStr).first()
            assertEquals(1, monthExpenses.size)
            val item = monthExpenses.first()
            assertEquals(100.0, item.amount, 0.001)
            assertEquals(i + 1, item.currentInstallment)
            assertEquals(12, item.totalInstallments)
            totalSum += item.amount
        }
        assertEquals(1200.0, totalSum, 0.001)
    }

    @Test
    fun `when editing existing installment series, clean up existing series first`() = runTest {
        val original = Expense(
            amount = 60.0,
            description = "Old Series",
            category = Category.SHOPPING,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = 2
        )
        useCase(original)

        val seriesBefore = repository.getExpenseSeries(1L)
        assertEquals(2, seriesBefore.size)

        // Now edit the series with 3 installments
        val edited = Expense(
            id = 1L,
            amount = 90.0,
            description = "Updated Series",
            category = Category.SHOPPING,
            date = baseDate,
            monthYear = "10-2026",
            totalInstallments = 3,
            currentInstallment = 1
        )
        useCase(edited)

        val month1Expenses = repository.getExpensesByMonth("10-2026").first()
        val newParentId = month1Expenses.first().id
        val allSeries = repository.getExpenseSeries(newParentId)
        assertTrue(allSeries.isNotEmpty())
        assertEquals(3, allSeries.size)
        assertEquals(30.0, allSeries.first().amount, 0.001)
        assertEquals("Updated Series", allSeries.first().description)
    }
}
