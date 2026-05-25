package com.kalex.bookyouu_notesapp.expenses.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.core.common.composables.BYScaffold
import com.kalex.bookyouu_notesapp.core.common.composables.LargeAmountInput
import com.kalex.bookyouu_notesapp.expenses.presentation.components.CategorySelector
import com.kalex.bookyouu_notesapp.expenses.R as ExpensesR
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AddExpenseRoot(
    viewModel: ExpenseViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    LaunchedEffect(state.editingExpenseId) {
        state.editingExpenseId?.let { id ->
            viewModel.getExpense(id)?.let { expense ->
                amount = String.format("%.0f", expense.amount)
                description = expense.description
                selectedCategory = expense.category
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ExpenseEvent.ExpenseSaved -> onNavigateBack()
                else -> Unit
            }
        }
    }

    AddExpenseScreen(
        state = state,
        amount = amount,
        onAmountChange = { amount = it },
        description = description,
        onDescriptionChange = { description = it },
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it },
        onDateChange = { viewModel.onAction(ExpenseAction.OnDateChange(it)) },
        onInstallmentsChange = { viewModel.onAction(ExpenseAction.OnInstallmentsChange(it)) },
        onSaveClick = {
            selectedCategory?.let { category ->
                viewModel.onAction(
                    ExpenseAction.OnSaveExpense(
                        amount = amount,
                        description = description,
                        category = category,
                        date = state.selectedDate,
                        installments = state.installments,
                        id = state.editingExpenseId
                    )
                )
            }
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    state: ExpenseState,
    amount: String,
    onAmountChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onInstallmentsChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter =
        remember { DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale.getDefault()) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDate.atStartOfDay(ZoneId.of("UTC"))
                .toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateChange(
                            Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    BYScaffold(
        topBarTitle = null,
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Amount Section
            LargeAmountInput(
                label = stringResource(ExpensesR.string.amount_label),
                amount = amount,
                onAmountChange = onAmountChange
            )

            // Category Section
            Column {
                Text(
                    text = stringResource(ExpensesR.string.category_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                CategorySelector(
                    modifier = Modifier.height(225.dp),
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected,
                )
            }

            // Description Section
            Column {
                Text(
                    text = stringResource(ExpensesR.string.description_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    placeholder = {
                        Text(
                            stringResource(ExpensesR.string.description_placeholder),
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f)
                    ),
                    singleLine = true
                )
            }

            // Installments Section
            Column {
                Text(
                    text = stringResource(ExpensesR.string.installments_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = state.installments,
                    onValueChange = {
                        if (it.isEmpty()) {
                            onInstallmentsChange(it)
                        } else {
                            val value = it.filter { char -> char.isDigit() }
                                .toIntOrNull()
                            if (value != null && value in 1..48) {
                                onInstallmentsChange(value.toString())
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            stringResource(ExpensesR.string.installments_placeholder),
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Date Section
            Column(
                modifier = Modifier.clickable { showDatePicker = true }
            ) {
                Text(
                    text = stringResource(ExpensesR.string.date_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val dateText = if (state.selectedDate == LocalDate.now()) {
                        "Hoy, ${
                            state.selectedDate.format(
                                DateTimeFormatter.ofPattern(
                                    "dd 'de' MMMM",
                                    Locale.getDefault()
                                )
                            )
                        }"
                    } else {
                        state.selectedDate.format(dateFormatter)
                    }
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                ),
                enabled = !state.isLoading && amount.isNotBlank() && description.isNotBlank() && selectedCategory != null
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    val text =
                        if (state.editingExpenseId != null) "Update Expense" else stringResource(
                            ExpensesR.string.save_expense
                        )
                    Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
