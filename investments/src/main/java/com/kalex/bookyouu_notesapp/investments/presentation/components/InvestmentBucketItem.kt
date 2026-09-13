package com.kalex.bookyouu_notesapp.investments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.investments.presentation.InvestmentType
import com.kalex.bookyouu_notesapp.investments.presentation.InvestmentUi

fun getInvestmentColors(type: InvestmentType): Pair<Color, Color> {
    return when (type) {
        InvestmentType.USD -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        InvestmentType.HIGH_YIELD_SAVINGS -> Pair(Color(0xFFE0F2F1), Color(0xFF00695C))
        InvestmentType.CDT -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        InvestmentType.STOCKS -> Pair(Color(0xFFEDE7F6), Color(0xFF6A1B9A))
        InvestmentType.BONDS -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        InvestmentType.MUTUAL_FUNDS -> Pair(Color(0xFFE8EAF6), Color(0xFF283593))
        InvestmentType.ETF -> Pair(Color(0xFFE0F7FA), Color(0xFF00838F))
        InvestmentType.REAL_ESTATE -> Pair(Color(0xFFF1F8E9), Color(0xFF33691E))
        InvestmentType.CRYPTO -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        InvestmentType.GENERAL -> Pair(Color(0xFFECEFF1), Color(0xFF37474F))
    }
}

@Composable
fun InvestmentBucketItem(
    investment: InvestmentUi,
    onClick: () -> Unit
) {
    val (backgroundColor, iconColor) = remember(investment.type) {
        getInvestmentColors(investment.type)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = investment.type.iconResId),
                    contentDescription = stringResource(id = investment.type.titleResId),
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = investment.name.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = investment.balance,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = backgroundColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = stringResource(id = investment.type.titleResId),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = iconColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

