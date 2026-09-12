package com.kalex.bookyouu_notesapp.investments.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.core.common.composables.LabeledInput
import com.kalex.bookyouu_notesapp.core.common.composables.LargeAmountInput
import com.kalex.bookyouu_notesapp.investments.R
import com.kalex.bookyouu_notesapp.investments.presentation.components.CurrencySelector
import com.kalex.bookyouu_notesapp.investments.presentation.components.InvestmentDaysSelector
import com.kalex.bookyouu_notesapp.investments.presentation.components.InvestmentTypeSelector
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInvestmentScreen(
    investmentId: Long = NON_INVESTMENT_ID,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AddInvestmentViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(investmentId) {
        if (investmentId > 0) {
            viewModel.onAction(AddInvestmentAction.LoadInvestment(investmentId))
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                AddInvestmentEvent.InvestmentCreated -> onSuccess()
                AddInvestmentEvent.InvestmentDeleted -> onSuccess()
                is AddInvestmentEvent.ShowError -> {
                    // TODO: Show snackbar or toast
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.isEditMode) stringResource(R.string.edit_investment_title) else stringResource(R.string.add_investment_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.add_investment_back_description))
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            LargeAmountInput(
                label = stringResource(R.string.add_investment_initial_amount_label),
                amount = state.amount,
                onAmountChange = { viewModel.onAction(AddInvestmentAction.OnAmountChange(it)) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            LabeledInput(
                label = stringResource(R.string.add_investment_name_label),
                value = state.name,
                onValueChange = { viewModel.onAction(AddInvestmentAction.OnNameChange(it)) },
                placeholder = stringResource(R.string.add_investment_name_placeholder)
            )

            Spacer(modifier = Modifier.height(24.dp))

            CurrencySelector(
                selectedCurrency = state.selectedCurrency,
                onCurrencySelected = { viewModel.onAction(AddInvestmentAction.OnCurrencyChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            InvestmentTypeSelector(
                selectedType = state.selectedType,
                onTypeSelected = { viewModel.onAction(AddInvestmentAction.OnTypeChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            InvestmentDaysSelector(
                selectedDays = state.term,
                onDaysSelected = { viewModel.onAction(AddInvestmentAction.OnTermChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledInput(
                label = stringResource(R.string.add_investment_revenue_label),
                value = state.annualRevenue,
                onValueChange = { viewModel.onAction(AddInvestmentAction.OnRevenueChange(it)) },
                placeholder = stringResource(R.string.add_investment_revenue_placeholder)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onAction(AddInvestmentAction.OnCreateInvestment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (state.isEditMode) stringResource(R.string.edit_investment_button_save) else stringResource(R.string.add_investment_button_create),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.isEditMode) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { viewModel.onAction(AddInvestmentAction.OnDeleteClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = stringResource(R.string.delete_investment_button),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

        }

        if (state.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onAction(AddInvestmentAction.OnDismissDeleteDialog) },
                title = { Text(stringResource(R.string.delete_investment_dialog_title)) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_investment_dialog_body,
                            state.name
                        )
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onAction(AddInvestmentAction.OnConfirmDelete) }
                    ) {
                        Text(
                            stringResource(R.string.delete_investment_confirm),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onAction(AddInvestmentAction.OnDismissDeleteDialog) }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}