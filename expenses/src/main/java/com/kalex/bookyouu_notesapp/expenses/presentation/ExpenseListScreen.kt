package com.kalex.bookyouu_notesapp.expenses.presentation

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.BaseScaffold
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
    onNavigateToAddExpense: () -> Unit,
    onNavigateToEditExpense: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ExpenseEvent.NavigateToAddExpense -> onNavigateToAddExpense()
                is ExpenseEvent.NavigateToEditExpense -> onNavigateToEditExpense(event.id)
                is ExpenseEvent.SharePdf -> {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, event.uri)
                        putExtra(Intent.EXTRA_SUBJECT, event.title)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val shareIntent = Intent.createChooser(
                        sendIntent,
                        context.getString(ExpensesR.string.share_pdf_title)
                    )
                    context.startActivity(shareIntent)
                }
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
    var expenseToDelete by remember { mutableStateOf<ExpenseUi?>(null) }

    if (expenseToDelete != null) {
        val targetExpense = expenseToDelete!!
        val current = targetExpense.currentInstallment ?: 1
        val total = targetExpense.totalInstallments ?: 1
        val parentId = targetExpense.parentExpenseId ?: targetExpense.id

        AlertDialog(
            onDismissRequest = { expenseToDelete = null },
            title = {
                Text(
                    text = stringResource(ExpensesR.string.delete_installment_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(
                        ExpensesR.string.delete_installment_message,
                        current,
                        total
                    )
                )
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onAction(ExpenseAction.OnDeleteExpense(targetExpense.id))
                            expenseToDelete = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(ExpensesR.string.delete_only_this, current, total))
                    }

                    OutlinedButton(
                        onClick = {
                            onAction(ExpenseAction.OnDeleteFutureInstallments(parentId, current))
                            expenseToDelete = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(ExpensesR.string.delete_future_installments))
                    }

                    OutlinedButton(
                        onClick = {
                            onAction(ExpenseAction.OnDeleteExpenseSeries(parentId))
                            expenseToDelete = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(ExpensesR.string.delete_all_installments))
                    }

                    TextButton(
                        onClick = { expenseToDelete = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(ExpensesR.string.cancel))
                    }
                }
            }
        )
    }

    BaseScaffold(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding() ),
        topBarTitle = stringResource(ExpensesR.string.expenses_title),
        actions = {
            if (state.expenses.isNotEmpty()) {
                if (state.isExportingPdf) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    IconButton(onClick = { onAction(ExpenseAction.OnExportPdfClick) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(ExpensesR.string.export_pdf),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
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
                .padding(top = padding.calculateTopPadding())
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
                            onDelete = {
                                if (expense.totalInstallments != null && expense.totalInstallments > 1) {
                                    expenseToDelete = expense
                                } else {
                                    onAction(ExpenseAction.OnDeleteExpense(expense.id))
                                }
                            },
                            onClick = { onAction(ExpenseAction.OnEditExpenseClick(expense.id)) }
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
