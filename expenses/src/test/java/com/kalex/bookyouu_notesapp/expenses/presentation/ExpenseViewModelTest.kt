package com.kalex.bookyouu_notesapp.expenses.presentation

import androidx.lifecycle.SavedStateHandle
import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.expenses.data.repository.FakeExpenseRepository
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.usecase.*
import com.kalex.bookyouu_notesapp.expenses.presentation.pdf.FakeMonthlyReportPdfExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var expenseRepository: FakeExpenseRepository
    private lateinit var pdfExporter: FakeMonthlyReportPdfExporter
    private lateinit var viewModel: ExpenseViewModel

    private val testMonth = YearMonth.of(2026, 10)
    private val testMonthYearStr = "10-2026"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        expenseRepository = FakeExpenseRepository()
        pdfExporter = FakeMonthlyReportPdfExporter()

        val getMonthlyExpensesUseCase = GetMonthlyExpensesUseCase(expenseRepository)
        val getMonthlySummaryUseCase = GetMonthlySummaryUseCase(expenseRepository)
        val addExpenseUseCase = AddExpenseUseCase(expenseRepository)
        val deleteExpenseUseCase = DeleteExpenseUseCase(expenseRepository)
        val getExpenseByIdUseCase = GetExpenseByIdUseCase(expenseRepository)
        val savedStateHandle = SavedStateHandle(mapOf("selected_month" to testMonth.toString()))

        viewModel = ExpenseViewModel(
            getMonthlyExpensesUseCase = getMonthlyExpensesUseCase,
            getMonthlySummaryUseCase = getMonthlySummaryUseCase,
            addExpenseUseCase = addExpenseUseCase,
            deleteExpenseUseCase = deleteExpenseUseCase,
            getExpenseByIdUseCase = getExpenseByIdUseCase,
            pdfGenerator = pdfExporter,
            savedStateHandle = savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when onExportPdfClick is triggered with expenses, pdfExporter is called`() = runTest(testDispatcher) {
        expenseRepository.insertExpense(
            Expense(
                id = 1,
                amount = 45.0,
                description = "Fuel",
                category = Category.TRANSPORT,
                date = LocalDateTime.of(2026, 10, 8, 9, 0),
                monthYear = testMonthYearStr
            )
        )
        advanceUntilIdle()

        viewModel.onAction(ExpenseAction.OnExportPdfClick)
        advanceUntilIdle()

        assertEquals(1, pdfExporter.generateCallCount)
        assertEquals(testMonthYearStr, pdfExporter.lastMonthYear)
    }

    @Test
    fun `when onExportPdfClick is triggered without expenses, pdfExporter is not called`() = runTest(testDispatcher) {
        advanceUntilIdle()

        viewModel.onAction(ExpenseAction.OnExportPdfClick)
        advanceUntilIdle()

        assertEquals(0, pdfExporter.generateCallCount)
    }

    @Test
    fun `when onDeleteExpense is triggered, expense is deleted from repository`() = runTest(testDispatcher) {
        expenseRepository.insertExpense(
            Expense(
                id = 1,
                amount = 100.0,
                description = "Clothes",
                category = Category.SHOPPING,
                date = LocalDateTime.of(2026, 10, 10, 15, 0),
                monthYear = testMonthYearStr
            )
        )
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.expenses.size)

        viewModel.onAction(ExpenseAction.OnDeleteExpense(1))
        advanceUntilIdle()

        assertEquals(0, viewModel.state.value.expenses.size)
    }

    @Test
    fun `when onMonthChange is triggered, state updates selected month and loads new expenses`() = runTest(testDispatcher) {
        val nextMonth = YearMonth.of(2026, 11)
        val nextMonthYearStr = "11-2026"
        expenseRepository.insertExpense(
            Expense(
                id = 2,
                amount = 30.0,
                description = "Coffee",
                category = Category.FOOD,
                date = LocalDateTime.of(2026, 11, 2, 10, 0),
                monthYear = nextMonthYearStr
            )
        )
        advanceUntilIdle()

        viewModel.onAction(ExpenseAction.OnMonthChange(nextMonth))
        advanceUntilIdle()

        assertEquals(nextMonth, viewModel.state.value.selectedMonth)
        assertEquals(1, viewModel.state.value.expenses.size)
        assertEquals("Coffee", viewModel.state.value.expenses.first().description)
    }
}
