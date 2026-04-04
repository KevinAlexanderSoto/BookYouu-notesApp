package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.payments.presentation.components.ObligationRow
import com.kalex.bookyouu_notesapp.payments.presentation.components.ObligationsScaffold
import com.kalex.bookyouu_notesapp.payments.presentation.components.OverviewCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ObligationsScreen(
    viewModel: ObligationsViewModel = koinViewModel(),
    onMenuClick: () -> Unit = {},
    onFloatingActionClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    ObligationsScaffold(
        title = "Obligations", //TODO: Use strings
        onNavigationClick = onMenuClick,
        onFloatingActionClick = onFloatingActionClick,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        OverviewCard(
                            totalBalance = uiState.totalBalance,
                            pendingAmount = uiState.pendingAmount,
                            paidAmount = uiState.paidAmount,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Upcoming Payments",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TextButton(onClick = { /* TODO: View All */ }) {
                                Text(
                                    text = "VIEW ALL",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    items(uiState.obligations) { obligation ->
                        ObligationRow(
                            obligation = obligation,
                            onToggle = { viewModel.onPaymentToggled(it) }
                        )
                    }

                    if (uiState.obligations.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No obligations found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
