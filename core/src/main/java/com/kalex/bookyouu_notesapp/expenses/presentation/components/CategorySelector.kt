package com.kalex.bookyouu_notesapp.expenses.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.core.common.Category
import com.kalex.bookyouu_notesapp.core.common.CategoryIcon

@Composable
fun CategorySelector(
    modifier: Modifier = Modifier.Companion,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(Category.values()) { category ->
            val isSelected = category == selectedCategory
            val backgroundColor =
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.3f
                )
            val contentColor =
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .clickable { onCategorySelected(category) },
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.Companion
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    val icon = category.icon as CategoryIcon.Resource
                    Icon(
                        painter = painterResource(id = icon.resId),
                        contentDescription = stringResource(category.displayNameRes),
                        tint = contentColor,
                        modifier = Modifier.Companion.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.Companion.height(8.dp))
                Text(
                    text = stringResource(category.displayNameRes),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Companion.Bold else FontWeight.Companion.Medium
                    ),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Companion.Center,
                    maxLines = 1
                )
            }
        }
    }
}