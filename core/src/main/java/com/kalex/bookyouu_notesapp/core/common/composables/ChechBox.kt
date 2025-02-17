package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BYRoundedCheckView(
    @StringRes text: Int,
    isRoundedChecked: (isChecked: Boolean) -> Unit,
) {
    val isChecked = remember { mutableStateOf(false) }

    val circleSize = remember { mutableStateOf(20.dp) }
    val circleThickness = remember { mutableStateOf(2.dp) }
    val color = remember { mutableStateOf(Color.Gray) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 4.dp)
            .toggleable(value = isChecked.value, role = Role.Checkbox) {
                isChecked.value = it
                isRoundedChecked(isChecked.value)
                if (isChecked.value) {
                    color.value = Color.Black
                } else {
                    color.value = Color.Gray
                }
            },
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(circleSize.value)
                .background(color.value)
                .padding(circleThickness.value)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked.value) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        }

        Text(
            text = stringResource(id = text),
            color = color.value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 5.dp),
        )
    }
}
