package com.kalex.bookyouu_notesapp.expenses.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldTopBar
import com.kalex.bookyouu_notesapp.core.common.composables.LabeledInput
import com.kalex.bookyouu_notesapp.expenses.domain.model.Category
import com.kalex.bookyouu_notesapp.expenses.presentation.components.CategorySelector
import kotlinx.coroutines.flow.collectLatest

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
        amount = amount,
        onAmountChange = { amount = it },
        description = description,
        onDescriptionChange = { description = it },
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it },
        onSaveClick = {
            selectedCategory?.let { category ->
                viewModel.onAction(ExpenseAction.OnSaveExpense(amount, description, category))
            }
        },
        onNavigateBack = onNavigateBack,
        isLoading = state.isLoading
    )
}

@Composable
fun AddExpenseScreen(
    amount: String,
    onAmountChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit,
    isLoading: Boolean
) {
    ScaffoldTopBar(
        title = "Add Expense",
        onBackNavigationClick = onNavigateBack
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledInput(
                label = "Amount",
                value = amount,
                onValueChange = onAmountChange,
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            LabeledInput(
                label = "Description",
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = "What did you spend on?",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            Text(
                text = "CATEGORY",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            CategorySelector(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                modifier = Modifier.weight(1f)
            )
            
            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && amount.isNotBlank() && description.isNotBlank() && selectedCategory != null
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save Expense")
                }
            }
        }
    }
}
