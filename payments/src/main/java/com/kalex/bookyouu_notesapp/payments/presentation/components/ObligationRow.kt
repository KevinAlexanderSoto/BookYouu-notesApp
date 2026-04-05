package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.kalex.bookyouu_notesapp.payments.R
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ObligationRow(
    obligation: Obligation,
    onToggle: (Obligation) -> Unit,
    onLongClick: (Obligation) -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 2
    }
    
    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    
    val alpha = if (obligation.isPaid) 0.6f else 1.0f
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        obligation.isPaid -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.surface
    }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onToggle(obligation) },
                onLongClick = { onLongClick(obligation) }
            ),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = 4.dp,
        shadowElevation = if (isSelected) 0.dp else 2.dp,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .alpha(alpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selection Icon or Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = getCategoryIcon(obligation.category),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Name and Date
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = obligation.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (obligation.isPaid) {
                        val formattedDate = obligation.lastPaidDate?.let { dateFormatter.format(it) } ?: "Unknown"
                        stringResource(R.string.paid_on, formattedDate)
                    } else {
                        stringResource(R.string.due_on, obligation.dayOfMonth)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount and Status
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (obligation.isPaid) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                            text = currencyFormatter.format(obligation.amount),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                            )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Badge
                            val badgeColor = if (obligation.isPaid) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
                            val badgeTextColor = if (obligation.isPaid) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer

                            Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = badgeColor
                            ) {
                            Text(
                            text = if (obligation.isPaid) stringResource(R.string.paid) else stringResource(R.string.pending),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp
                            ),
                            color = badgeTextColor
                            )
                            }

            }
        }
    }
}

@Composable
fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "home", "rent" -> Icons.Default.Home
        "gym", "fitness" -> Icons.Default.Info //TODO: Set the correct icon
        "internet", "web", "tech" -> Icons.Default.AccountBox //TODO: Set the correct icon
        "electricity", "utility", "power" -> Icons.Default.Build //TODO: Set the correct icon
        else -> Icons.Default.Face //TODO: Set the correct icon
    }
}
