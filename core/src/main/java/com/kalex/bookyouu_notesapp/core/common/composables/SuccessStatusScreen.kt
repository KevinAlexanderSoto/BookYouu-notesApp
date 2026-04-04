package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessStatusScreen(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Success Icon Area
        Box(
            modifier = Modifier
                .size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background surface variant circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(colorScheme.surfaceVariant)
            )
            
            // Decorative tertiary container circle (top right)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colorScheme.tertiaryContainer)
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
            )

            // Main primary circle with checkmark
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = title,
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = message,
            style = TextStyle(
                fontSize = 16.sp,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1.2f))

        // Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onPrimaryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = primaryButtonText,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }

            if (secondaryButtonText != null && onSecondaryClick != null) {
                OutlinedButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(colorScheme.outline)
                    )
                ) {
                    Text(
                        text = secondaryButtonText,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
