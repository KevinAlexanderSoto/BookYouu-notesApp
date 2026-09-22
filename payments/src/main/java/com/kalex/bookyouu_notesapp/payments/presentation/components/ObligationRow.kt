package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.core.common.CategoryIcon
import com.kalex.bookyouu_notesapp.core.common.getCategoryColors
import com.kalex.bookyouu_notesapp.payments.R
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
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
    val category = remember(obligation.category) { Category.fromName(obligation.category) }
    val (categoryBg, categoryIconColor) = remember(category) { getCategoryColors(category) }

    val iconBoxBg = when {
        isSelected -> MaterialTheme.colorScheme.primary
        obligation.isPaid -> categoryBg.copy(alpha = 0.35f)
        else -> categoryBg
    }

    val iconTint = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        obligation.isPaid -> categoryIconColor.copy(alpha = 0.45f)
        else -> categoryIconColor
    }

    val cardBg = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        obligation.isPaid -> Color(0xFFFAFAFA)
        else -> Color.White
    }

    val scope = rememberCoroutineScope()
    val boxDismissState = rememberSwipeToDismissBoxState(SwipeToDismissBoxValue.Settled)

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
                if (icon != null) {
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
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEditClick(obligation)
                    scope.launch { boxDismissState.reset() }
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteClick(obligation)
                }
                SwipeToDismissBoxValue.Settled -> {}
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
            color = cardBg,
            tonalElevation = if (obligation.isPaid) 0.dp else 1.dp,
            shadowElevation = if (isSelected || obligation.isPaid) 0.dp else 2.dp,
            border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon with Investments-matching 14dp rounded box
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    } else {
                        when (val icon = category.icon) {
                            is CategoryIcon.Resource -> {
                                Icon(
                                    painter = painterResource(id = icon.resId),
                                    contentDescription = stringResource(category.displayNameRes),
                                    tint = iconTint,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Name, Category Badge & Due Date
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = obligation.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (obligation.isPaid) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = categoryBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = stringResource(id = category.displayNameRes),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = categoryIconColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = if (obligation.isPaid) {
                                val formattedDate = obligation.lastPaidDate?.let { dateFormatter.format(it) } ?: "Unknown"
                                stringResource(R.string.paid_on, formattedDate)
                            } else {
                                stringResource(R.string.due_on, obligation.dayOfMonth)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))

                // Amount and Status Badge
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = currencyFormatter.format(obligation.amount),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (obligation.isPaid) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val badgeBg = if (obligation.isPaid) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    val badgeTextColor = if (obligation.isPaid) Color(0xFF2E7D32) else Color(0xFFC62828)

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = badgeBg
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
