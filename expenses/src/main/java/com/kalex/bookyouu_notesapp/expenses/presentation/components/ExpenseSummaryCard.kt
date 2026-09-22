package com.kalex.bookyouu_notesapp.expenses.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpenseSummaryCard(
    totalSpent: String,
    monthName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF004D40),
                            Color(0xFF00695C)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Column {
                Text(
                    text = "Total Spent in $monthName".uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = totalSpent,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = Color.White
                )
            }
        }
    }
}
