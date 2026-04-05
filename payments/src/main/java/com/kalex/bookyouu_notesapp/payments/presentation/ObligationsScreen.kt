package com.kalex.bookyouu_notesapp.payments.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.kalex.bookyouu_notesapp.payments.R
import com.kalex.bookyouu_notesapp.payments.presentation.components.ObligationRow
import com.kalex.bookyouu_notesapp.payments.presentation.components.ObligationsScaffold
import com.kalex.bookyouu_notesapp.payments.presentation.components.OverviewCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObligationsScreen(
    viewModel: ObligationsViewModel = koinViewModel(),
    onMenuClick: () -> Unit = {},
    onFloatingActionClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    ObligationsScaffold(
        title = stringResource(R.string.payments_title),
        onFloatingActionClick = onFloatingActionClick,
        isSelectionMode = uiState.isSelectionMode,
        selectedCount = uiState.selectedObligations.size,
        onClearSelection = { viewModel.clearSelection() },
        onDeleteSelected = { viewModel.deleteSelectedObligations() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 4.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    item {
                        OverviewCard(
                            totalBalance = uiState.totalBalance,
                            pendingAmount = uiState.pendingAmount,
                            paidAmount = uiState.paidAmount,
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.upcoming_payments),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TextButton(onClick = { /* TODO: View All */ }) {
                                Text(
                                    text = stringResource(R.string.view_all),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    items(
                        items = uiState.obligations,
                        key = { it.id }
                    ) { obligation ->
                        val isSelected = uiState.selectedObligations.contains(obligation.id)

                        val dismissState = rememberSwipeToDismissBoxState()

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                AnimatedVisibility(dismissState.targetValue == SwipeToDismissBoxValue.EndToStart){
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.alpha(1f)
                                    )
                                }
                                }
                            },
                            onDismiss = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.onDeleteObligation(obligation.id)
                                }
                            }
                        ) {
                            ObligationRow(
                                obligation = obligation,
                                isSelected = isSelected,
                                onToggle = { viewModel.onPaymentToggled(it) },
                                onLongClick = { viewModel.toggleSelection(it.id) },
                                modifier = Modifier.animateItem()
                            )
                        }
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
                                    text = stringResource(R.string.no_obligations),
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
