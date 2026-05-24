package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.kalex.bookyouu_notesapp.payments.R
import com.kalex.bookyouu_notesapp.core.common.composables.SuccessStatusScreen
import com.kalex.bookyouu_notesapp.core.common.composables.LabeledInput
import com.kalex.bookyouu_notesapp.expenses.presentation.components.CategorySelector
import com.kalex.bookyouu_notesapp.payments.presentation.components.PaymentFrequencyCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ObligationsCreateRoot(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
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
        onBack = onBack
    )
}

@Composable
fun ObligationsCreateScreen(
    state: ObligationsCreateState,
    onAction: (ObligationsCreateAction) -> Unit,
    onBack: () -> Unit,
    paddingValues: PaddingValues,
) {
    val isEditing = state.editingObligationId != null

    if (state.isSuccess) {
        SuccessStatusScreen(
            title = if (isEditing) "Obligation Updated" else stringResource(R.string.obligation_created_title),
            message = if (isEditing) "Your obligation '${state.name}' has been updated successfully." else stringResource(R.string.obligation_created_message, state.name),
            primaryButtonText = stringResource(R.string.done),
            onPrimaryClick = onBack,
            secondaryButtonText = if (isEditing) null else stringResource(R.string.add_another),
            onSecondaryClick = { onAction(ObligationsCreateAction.OnResetClick) }
        )
    } else {
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    val buttonText = if (isEditing) "Update Obligation" else stringResource(R.string.save_obligation)
                    Text(buttonText, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
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
            Text(
                text = if (isEditing) "EDIT ENTRY" else stringResource(R.string.new_entry),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = if (isEditing) "Update Commitment" else stringResource(R.string.define_commitment),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
            ) {
                HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 4.dp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            LabeledInput(
                label = stringResource(R.string.obligation_name_label),
                value = state.name,
                onValueChange = { onAction(ObligationsCreateAction.OnNameChange(it)) },
                placeholder = stringResource(R.string.obligation_name_placeholder)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledInput(
                label = stringResource(R.string.amount_label),
                value = state.amount,
                onValueChange = { onAction(ObligationsCreateAction.OnAmountChange(it)) },
                placeholder = stringResource(R.string.amount_placeholder),
                leadingIcon = {
                    Text(
                        "$",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
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

            Text(
                text = stringResource(R.string.reminder_label),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.enable_monthly_reminder),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Switch(
                    checked = state.isReminderEnabled,
                    onCheckedChange = { onAction(ObligationsCreateAction.OnReminderToggle(it)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            CategorySelector(
                modifier = Modifier.height(225.dp),
                selectedCategory = state.category,
                onCategorySelected = { onAction(ObligationsCreateAction.OnCategoryChange(it)) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
}
