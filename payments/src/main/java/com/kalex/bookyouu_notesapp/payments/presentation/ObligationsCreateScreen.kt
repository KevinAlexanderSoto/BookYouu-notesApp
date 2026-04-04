package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.payments.presentation.components.CategoryGrid
import com.kalex.bookyouu_notesapp.payments.presentation.components.LabeledInput
import com.kalex.bookyouu_notesapp.payments.presentation.components.PaymentFrequencyCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ObligationsCreateRoot(
    paddingValues: PaddingValues,
    viewModel: ObligationsCreateViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Simple event observation
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ObligationsCreateEvent.ObligationSaved -> { /* Show confirmation screen */ }
                is ObligationsCreateEvent.ShowError -> { /* Show snackbar */ }
            }
        }
    }

    ObligationsCreateScreen(
        paddingValues = paddingValues,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun ObligationsCreateScreen(
    state: ObligationsCreateState,
    onAction: (ObligationsCreateAction) -> Unit,
    paddingValues: PaddingValues,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        bottomBar = {
            Button(
                onClick = { onAction(ObligationsCreateAction.OnSaveClick) },
                enabled = state.isSaveEnabled && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004D40),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Obligation", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "NEW ENTRY",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D40)
                )
            )

            Text(
                text = "Define Your Commitment",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            )

            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
            ) {
                Divider(color = Color(0xFF004D40), thickness = 4.dp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            LabeledInput(
                label = "OBLIGATION NAME",
                value = state.name,
                onValueChange = { onAction(ObligationsCreateAction.OnNameChange(it)) },
                placeholder = "e.g. Rent, Netflix, Car Loan"
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledInput(
                label = "AMOUNT",
                value = state.amount,
                onValueChange = { onAction(ObligationsCreateAction.OnAmountChange(it)) },
                placeholder = "0.00",
                leadingIcon = {
                    Text(
                        "$",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF004D40)
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PaymentFrequencyCard(
                selectedFrequency = state.frequency,
                onFrequencySelected = { onAction(ObligationsCreateAction.OnFrequencyChange(it)) },
                selectedDay = state.dayOfMonth,
                onDaySelected = { onAction(ObligationsCreateAction.OnDayChange(it)) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            CategoryGrid(
                selectedCategory = state.category,
                onCategorySelected = { onAction(ObligationsCreateAction.OnCategoryChange(it)) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
