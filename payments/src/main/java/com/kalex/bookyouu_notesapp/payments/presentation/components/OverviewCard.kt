package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.current_balance).uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormatter.format(totalBalance),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.pending).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currencyFormatter.format(pendingAmount),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFFFCDD2)
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.height(26.dp),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.2f)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.paid).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currencyFormatter.format(paidAmount),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFB2DFDB)
                        )
                    }
                }
            }
        }
    }
}
