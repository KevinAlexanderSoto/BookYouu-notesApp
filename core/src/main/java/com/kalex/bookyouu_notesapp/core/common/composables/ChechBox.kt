package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BYRoundedCheckView(
    text: String,
    isRoundedChecked: (isChecked: Boolean) -> Unit,
) {
    val isChecked = remember { mutableStateOf(false) }

    val circleSize = 20.dp
    val circleThickness = 2.dp
    val color = if (isChecked.value) Color.Black else Color.Gray
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 4.dp)
            .toggleable(value = isChecked.value, role = Role.Checkbox) {
                isChecked.value = it
                isRoundedChecked(isChecked.value)
            },
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(circleSize)
                .background(color)
                .padding(circleThickness)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked.value) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            }
        }

        Text(
            text = text,
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 5.dp),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun BYRoundedCheckViewPreview() {
    // Using a placeholder string resource ID for demonstration
    BYRoundedCheckView(
        "Text", // Replace with actual string resource
        isRoundedChecked = { isChecked ->
            // Dummy implementation for preview
        }
    )
}
