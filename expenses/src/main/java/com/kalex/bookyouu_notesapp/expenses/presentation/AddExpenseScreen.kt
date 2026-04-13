package com.kalex.bookyouu_notesapp.expenses.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.core.common.composables.BYScaffold
import com.kalex.bookyouu_notesapp.core.common.composables.LargeAmountInput
import com.kalex.bookyouu_notesapp.expenses.domain.model.Category
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
        onSaveClick = {
            selectedCategory?.let { category ->
                viewModel.onAction(ExpenseAction.OnSaveExpense(amount, description, category, state.selectedDate))
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
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale.getDefault()) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC")).toLocalDate()
                    return date.month == state.selectedMonth.month && date.year == state.selectedMonth.year
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateChange(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate())
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
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.height(200.dp)
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
                    placeholder = { Text(stringResource(ExpensesR.string.description_placeholder), color = Color.LightGray) },
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
                        "Hoy, ${state.selectedDate.format(DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale.getDefault()))}"
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
                    containerColor = Color(0xFF2D5D57)
                ),
                enabled = !state.isLoading && amount.isNotBlank() && description.isNotBlank() && selectedCategory != null
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(ExpensesR.string.save_expense), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
