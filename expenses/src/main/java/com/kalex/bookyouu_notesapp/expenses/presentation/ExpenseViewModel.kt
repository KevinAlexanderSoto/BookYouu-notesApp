package com.kalex.bookyouu_notesapp.expenses.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.expenses.domain.model.Expense
import com.kalex.bookyouu_notesapp.expenses.domain.usecase.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class ExpenseViewModel(
    private val getMonthlyExpensesUseCase: GetMonthlyExpensesUseCase,
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val amountFormatter = DecimalFormat("#,##0.00")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

    private val _state = MutableStateFlow(ExpenseState())
    val state = _state.asStateFlow()

    private val _events = Channel<ExpenseEvent>()
    val events = _events.receiveAsFlow()

    init {
        val savedMonth = savedStateHandle.get<String>("selected_month")
        val initialMonth = savedMonth?.let { YearMonth.parse(it) } ?: YearMonth.now()
        
        _state.update { it.copy(selectedMonth = initialMonth) }
        loadExpenses(initialMonth)
    }

    fun onAction(action: ExpenseAction) {
        when (action) {
            is ExpenseAction.OnMonthChange -> {
                savedStateHandle["selected_month"] = action.newMonth.toString()
                _state.update { it.copy(selectedMonth = action.newMonth) }
                loadExpenses(action.newMonth)
            }
            is ExpenseAction.OnDeleteExpense -> {
                viewModelScope.launch {
                    deleteExpenseUseCase(action.id)
                }
            }
            ExpenseAction.OnAddExpenseClick -> {
                viewModelScope.launch {
                    _events.send(ExpenseEvent.NavigateToAddExpense)
                }
            }
            is ExpenseAction.OnSaveExpense -> {
                saveExpense(action)
            }
        }
    }

    private fun loadExpenses(month: YearMonth) {
        val monthYearStr = "${month.monthValue.toString().padStart(2, '0')}-${month.year}"
        getMonthlySummaryUseCase(monthYearStr)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { summary ->
                _state.update { state ->
                    state.copy(
                        expenses = summary.expenses.map { it.toUiModel() },
                        totalSpent = "$ ${amountFormatter.format(summary.totalSpent)}",
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = UiText.DynamicString(e.message ?: "Unknown Error")) }
            }
            .launchIn(viewModelScope)
    }

    private fun saveExpense(action: ExpenseAction.OnSaveExpense) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val amount = action.amount.toDoubleOrNull() ?: 0.0
                val now = LocalDateTime.now()
                val monthYearStr = "${now.monthValue.toString().padStart(2, '0')}-${now.year}"
                
                val newExpense = Expense(
                    amount = amount,
                    description = action.description,
                    category = action.category,
                    date = now,
                    monthYear = monthYearStr
                )
                addExpenseUseCase(newExpense)
                _events.send(ExpenseEvent.ExpenseSaved)
            } catch (e: Exception) {
                _events.send(ExpenseEvent.ShowSnackbar(UiText.DynamicString(e.message ?: "Error saving expense")))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun Expense.toUiModel(): ExpenseUi {
        return ExpenseUi(
            id = id,
            amount = "$ ${amountFormatter.format(amount)}",
            description = description,
            category = category,
            date = date.format(dateFormatter)
        )
    }
}
