package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import com.kalex.bookyouu_notesapp.payments.R
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.core.common.CategoryIcon
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObligationRow(
    modifier: Modifier = Modifier,
    obligation: Obligation,
    onToggle: (Obligation) -> Unit,
    onLongClick: (Obligation) -> Unit,
    onEditClick: (Obligation) -> Unit,
    onDeleteClick: (Obligation) -> Unit,
    isSelected: Boolean = false,
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
    }
    
    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        obligation.isPaid -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surface
    }
    val scope = rememberCoroutineScope()
    val boxDismissState =
        rememberSwipeToDismissBoxState(
            SwipeToDismissBoxValue.Settled,
        )

    SwipeToDismissBox(
        modifier = modifier,
        state = boxDismissState,
        backgroundContent = {
            val alignment = when (boxDismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }
            val color = when (boxDismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                else -> Color.Transparent
            }
            val icon = when (boxDismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> null
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                if(icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (boxDismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        },
       onDismiss = { dismissState ->
           when (dismissState) {
               SwipeToDismissBoxValue.StartToEnd -> { onEditClick(obligation)
                   scope.launch {  boxDismissState.reset() }
               }
               SwipeToDismissBoxValue.EndToStart -> { onDeleteClick(obligation) }
               SwipeToDismissBoxValue.Settled -> { }
           }
       }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onToggle(obligation) },
                    onLongClick = { onLongClick(obligation) }
                ),
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            tonalElevation = if (obligation.isPaid) 0.dp else 1.dp,
            shadowElevation = if (isSelected || obligation.isPaid) 0.dp else 2.dp,
            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selection Icon or Category Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else if (obligation.isPaid) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
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
                        val categoryObj = Category.fromName(obligation.category)
                        val tint = if (obligation.isPaid) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.secondary
                        
                        when (val icon = categoryObj.icon) {
                            is CategoryIcon.Resource -> {
                                Icon(
                                    painter = painterResource(id = icon.resId),
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Name and Date
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = obligation.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                        )
                        if (obligation.isPaid) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = if (obligation.isPaid) {
                            val formattedDate = obligation.lastPaidDate?.let { dateFormatter.format(it) } ?: "Unknown"
                            stringResource(R.string.paid_on, formattedDate)
                        } else {
                            stringResource(R.string.due_on, obligation.dayOfMonth)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Amount and Status
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = currencyFormatter.format(obligation.amount),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Badge
                    val badgeColor = if (obligation.isPaid) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
                    val badgeTextColor = if (obligation.isPaid) MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onErrorContainer

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
}
