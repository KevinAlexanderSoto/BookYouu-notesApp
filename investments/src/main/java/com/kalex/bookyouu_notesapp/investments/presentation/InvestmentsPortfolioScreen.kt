package com.kalex.bookyouu_notesapp.investments.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.core.common.composables.ScaffoldFloatingButtonAndTopBar
import com.kalex.bookyouu_notesapp.investments.presentation.components.InvestmentBucketItem
import com.kalex.bookyouu_notesapp.investments.presentation.components.NetWorthCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvestmentsPortfolioScreen(
    paddingValues: PaddingValues,
    onNavigateToAddInvestment: () -> Unit,
    viewModel: InvestmentListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                InvestmentsEvent.NavigateToAddInvestment -> onNavigateToAddInvestment()
                is InvestmentsEvent.ShowError -> {
                    // Handle error
                }
            }
        }
    }

    ScaffoldFloatingButtonAndTopBar(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        title = "Investments",
        showNavigationIcon = false,
        onBackNavigationClick = {},
        onFloatingActionClick = { viewModel.onAction(InvestmentsAction.OnAddInvestmentClick) },
    ){ padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NetWorthCard(
                    totalNetWorth = state.totalNetWorth
                )
            }
            item {
                Column {
                    Text(
                        text = "Investment Buckets",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Allocation across your primary asset classes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(state.investments) { investment ->
                InvestmentBucketItem(
                    investment = investment,
                    onClick = { viewModel.onAction(InvestmentsAction.OnInvestmentClick(investment.id)) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
