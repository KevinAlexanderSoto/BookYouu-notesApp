package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.payments.domain.model.PaymentFrequency

@Composable
fun PaymentFrequencyCard(
    selectedFrequency: PaymentFrequency,
    onFrequencySelected: (PaymentFrequency) -> Unit,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = androidx.compose.material3.MaterialTheme.colorScheme
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colorScheme.surfaceVariant.copy(alpha = 0.5f), // Using surfaceVariant
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "PAYMENT FREQUENCY",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SegmentedToggleButton(
                    options = PaymentFrequency.entries,
                    selectedOption = selectedFrequency,
                    onOptionSelected = onFrequencySelected
                )
            }

            Column {
                Text(
                    text = if (selectedFrequency == PaymentFrequency.WEEKLY) "DAY OF WEEK" else "DAY OF MONTH",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DaySelector(
                    days = if (selectedFrequency == PaymentFrequency.WEEKLY) (1..7).toList() else (1..31).toList(),
                    selectedDay = selectedDay,
                    onDaySelected = onDaySelected
                )
            }
        }
    }
}

@Composable
fun SegmentedToggleButton(
    options: List<PaymentFrequency>,
    selectedOption: PaymentFrequency,
    onOptionSelected: (PaymentFrequency) -> Unit
) {
    val colorScheme = androidx.compose.material3.MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colorScheme.outlineVariant.copy(alpha = 0.3f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) colorScheme.surface else Color.Transparent)
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun DaySelector(
    days: List<Int>,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {
    val colorScheme = androidx.compose.material3.MaterialTheme.colorScheme
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            val isSelected = day == selectedDay
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) colorScheme.primary else colorScheme.outlineVariant.copy(alpha = 0.3f))
                    .clickable { onDaySelected(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                    )
                )
            }
        }
    }
}
