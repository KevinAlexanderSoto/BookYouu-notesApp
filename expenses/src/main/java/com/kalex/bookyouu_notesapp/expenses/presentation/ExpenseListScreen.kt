package com.kalex.bookyouu_notesapp.expenses.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.BYScaffold
import com.kalex.bookyouu_notesapp.core.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.expenses.presentation.components.ExpenseRow
import com.kalex.bookyouu_notesapp.expenses.presentation.components.ExpenseSummaryCard
import com.kalex.bookyouu_notesapp.expenses.R as ExpensesR
import com.kalex.bookyouu_notesapp.core.R as CoreR
import kotlinx.coroutines.flow.collectLatest
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ExpenseListRoot(
    viewModel: ExpenseViewModel,
    paddingValues: PaddingValues,
    onNavigateToAddExpense: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ExpenseEvent.NavigateToAddExpense -> onNavigateToAddExpense()
                else -> Unit
            }
        }
    }

    ExpenseListScreen(
        state = state,
        paddingValues = paddingValues,
        onAction = viewModel::onAction
    )
}

@Composable
fun ExpenseListScreen(
    state: ExpenseState,
    paddingValues: PaddingValues,
    onAction: (ExpenseAction) -> Unit
) {
    BYScaffold(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding() ),
        topBarTitle = "Expenses",
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(ExpenseAction.OnAddExpenseClick) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            MonthNavigator(
                selectedMonth = state.selectedMonth,
                onMonthChange = { onAction(ExpenseAction.OnMonthChange(it)) }
            )
            
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    BYLoadingIndicator()
                }
            } else if (state.expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyScreen(
                        title = UiText.StringResource(ExpensesR.string.empty_expenses_title),
                        description = UiText.StringResource(ExpensesR.string.empty_expenses_description),
                        mainIcon = CoreR.drawable.circle_help_svgrepo_com,
                        buttonText = UiText.StringResource(ExpensesR.string.empty_expenses_button),
                        onButtonClick = { onAction(ExpenseAction.OnAddExpenseClick) }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        ExpenseSummaryCard(
                            totalSpent = state.totalSpent,
                            monthName = state.selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        )
                    }
                    
                    items(state.expenses, key = { it.id }) { expense ->
                        ExpenseRow(
                            expense = expense,
                            onDelete = { onAction(ExpenseAction.OnDeleteExpense(expense.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonthNavigator(
    selectedMonth: java.time.YearMonth,
    onMonthChange: (java.time.YearMonth) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMonthChange(selectedMonth.minusMonths(1)) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
        }
        
        Text(
            text = "${selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${selectedMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        IconButton(onClick = { onMonthChange(selectedMonth.plusMonths(1)) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}
