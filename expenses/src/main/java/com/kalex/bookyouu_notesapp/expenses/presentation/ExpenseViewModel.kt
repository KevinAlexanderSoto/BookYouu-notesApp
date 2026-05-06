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
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class ExpenseViewModel(
    private val getMonthlyExpensesUseCase: GetMonthlyExpensesUseCase,
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
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
        
        _state.update { it.copy(
            selectedMonth = initialMonth,
            selectedDate = if (initialMonth == YearMonth.now()) LocalDate.now() else initialMonth.atDay(1)
        ) }

        val expenseId = savedStateHandle.get<Long>("expenseId")
        if (expenseId != null && expenseId != -1L) {
            loadExpenseForEditing(expenseId)
        }

        loadExpenses(initialMonth)
    }

    private fun loadExpenseForEditing(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, editingExpenseId = id) }
            val expense = getExpenseByIdUseCase(id)
            if (expense != null) {
                _state.update { it.copy(
                    selectedDate = expense.date.toLocalDate(),
                    isLoading = false
                ) }
                // We'll pass these to the UI via a one-time event or just state
                // For now, let's keep the editingExpenseId in state
            } else {
                _state.update { it.copy(isLoading = false, editingExpenseId = null) }
            }
        }
    }

    fun onAction(action: ExpenseAction) {
        when (action) {
            is ExpenseAction.OnMonthChange -> {
                savedStateHandle["selected_month"] = action.newMonth.toString()
                _state.update { it.copy(
                    selectedMonth = action.newMonth,
                    selectedDate = if (action.newMonth == YearMonth.now()) LocalDate.now() else action.newMonth.atDay(1)
                ) }
                loadExpenses(action.newMonth)
            }
            is ExpenseAction.OnDateChange -> {
                _state.update { it.copy(selectedDate = action.newDate) }
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
            is ExpenseAction.OnEditExpenseClick -> {
                viewModelScope.launch {
                    _events.send(ExpenseEvent.NavigateToEditExpense(action.id))
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
                val amount = action.amount.replace(",", "").toDoubleOrNull() ?: 0.0
                val dateTime = action.date.atTime(LocalTime.now())
                val monthYearStr = "${dateTime.monthValue.toString().padStart(2, '0')}-${dateTime.year}"
                
                val expense = Expense(
                    id = action.id ?: 0,
                    amount = amount,
                    description = action.description,
                    category = action.category,
                    date = dateTime,
                    monthYear = monthYearStr
                )
                addExpenseUseCase(expense)
                _events.send(ExpenseEvent.ExpenseSaved)
            } catch (e: Exception) {
                _events.send(ExpenseEvent.ShowSnackbar(UiText.DynamicString(e.message ?: "Error saving expense")))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
    
    suspend fun getExpense(id: Long): Expense? = getExpenseByIdUseCase(id)


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
