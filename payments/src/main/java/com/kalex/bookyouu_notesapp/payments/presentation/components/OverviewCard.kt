package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.kalex.bookyouu_notesapp.payments.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OverviewCard(
    totalBalance: Double,
    pendingAmount: Double,
    paidAmount: Double,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 2
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.current_balance),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyFormatter.format(totalBalance),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.pending),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currencyFormatter.format(pendingAmount),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                VerticalDivider(
                    modifier = Modifier.height(40.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.paid),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currencyFormatter.format(paidAmount),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}
