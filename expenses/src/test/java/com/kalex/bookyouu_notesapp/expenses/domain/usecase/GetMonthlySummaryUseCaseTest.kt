package com.kalex.bookyouu_notesapp.expenses.domain.usecase

import com.kalex.bookyouu_notesapp.expenses.data.repository.FakeExpenseRepository
import com.kalex.bookyouu_notesapp.expenses.domain.model.Category
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GetMonthlySummaryUseCaseTest {

    private lateinit var repository: FakeExpenseRepository
    private lateinit var getMonthlySummaryUseCase: GetMonthlySummaryUseCase

    @Before
    fun setUp() {
        repository = FakeExpenseRepository()
        getMonthlySummaryUseCase = GetMonthlySummaryUseCase(repository)
    }

    @Test
    fun `when expenses exist, summary calculates total correctly`() = runBlocking {
        // Given
        val monthYear = "10-2023"
        repository.insertExpense(Expense(amount = 100.0, description = "A", category = Category.FOOD, date = LocalDateTime.now(), monthYear = monthYear))
        repository.insertExpense(Expense(amount = 50.0, description = "B", category = Category.TRANSPORT, date = LocalDateTime.now(), monthYear = monthYear))
        repository.insertExpense(Expense(amount = 200.0, description = "C", category = Category.FOOD, date = LocalDateTime.now(), monthYear = "11-2023")) // Different month

        // When
        val summary = getMonthlySummaryUseCase(monthYear).first()

        // Then
        assertEquals(150.0, summary.totalSpent, 0.01)
        assertEquals(2, summary.expenses.size)
    }

    @Test
    fun `when no expenses, summary is empty`() = runBlocking {
        // Given
        val monthYear = "10-2023"

        // When
        val summary = getMonthlySummaryUseCase(monthYear).first()

        // Then
        assertEquals(0.0, summary.totalSpent, 0.01)
        assertEquals(0, summary.expenses.size)
        assertEquals(0, summary.categoryPercentages.size)
    }
}
