package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.kalex.bookyouu_notesapp.payments.R
import com.kalex.bookyouu_notesapp.payments.domain.model.ObligationCategory

@Composable
fun CategoryGrid(
    selectedCategory: ObligationCategory?,
    onCategorySelected: (ObligationCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = androidx.compose.material3.MaterialTheme.colorScheme
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.select_category),
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp) // Fixed height to avoid infinite height in Column
        ) {
            items(ObligationCategory.entries) { category ->
                CategoryCard(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: ObligationCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = androidx.compose.material3.MaterialTheme.colorScheme
    val icon = when (category) { // TODO: chnage the icons
        ObligationCategory.HOUSE -> Icons.Default.Home
        ObligationCategory.INTERNET -> Icons.Filled.Warning
        ObligationCategory.GYM -> Icons.Default.Home
        ObligationCategory.UTILITY -> Icons.Default.Face
        ObligationCategory.FOOD -> Icons.Default.Build
        ObligationCategory.TRANSPORT -> Icons.Default.AddCircle
    }

    Box(
        modifier = Modifier
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colorScheme.surface else colorScheme.surfaceVariant)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.name,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
