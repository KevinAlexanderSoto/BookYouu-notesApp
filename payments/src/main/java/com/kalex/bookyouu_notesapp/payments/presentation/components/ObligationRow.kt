package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ObligationRow(
    obligation: Obligation,
    onToggle: (Obligation) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 2
    }
    
    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    
    val alpha = if (obligation.isPaid) 0.6f else 1.0f
    val backgroundColor = if (obligation.isPaid) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle(obligation) },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .alpha(alpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(obligation.category),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
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
                        "Paid $formattedDate"
                    } else {
                        "Due ${obligation.dayOfMonth}" // Simple day display
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
                            text = if (obligation.isPaid) "PAID" else "PENDING",
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
