package com.kalex.bookyouu_notesapp.investments.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.kalex.bookyouu_notesapp.investments.presentation.components.InvestmentDaysSelector
import com.kalex.bookyouu_notesapp.investments.presentation.components.InvestmentTypeSelector
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInvestmentScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AddInvestmentViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                AddInvestmentEvent.InvestmentCreated -> onSuccess()
                is AddInvestmentEvent.ShowError -> {
                    // TODO: Show snackbar or toast
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.add_investment_title), fontWeight = FontWeight.Bold) },
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
                Text(stringResource(R.string.add_investment_button_create), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
}