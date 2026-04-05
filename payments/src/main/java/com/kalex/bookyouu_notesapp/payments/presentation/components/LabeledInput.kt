package com.kalex.bookyouu_notesapp.payments.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun LabeledInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    val visualTransformation = remember(keyboardOptions.keyboardType) {
        if (keyboardOptions.keyboardType == KeyboardType.Number || 
            keyboardOptions.keyboardType == KeyboardType.Decimal) {
            NumberVisualTransformation()
        } else {
            VisualTransformation.None
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    leadingIcon()
                }
            }
            TextField(
                value = value,
                onValueChange = { newRawValue ->
                    // Always pass the raw, unformatted input to the callback
                    // Remove any non-digit characters if it's a number input (optional, but safer)
                    val cleanValue = if (keyboardOptions.keyboardType == KeyboardType.Number) {
                        newRawValue.filter { it.isDigit() }
                    } else {
                        newRawValue
                    }
                    onValueChange(cleanValue)
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f),
                ),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                singleLine = true
            )
        }
    }
}

internal class NumberVisualTransformation : VisualTransformation {
    private val symbols = DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'
    }
    private val decimalFormat = DecimalFormat("#,###", symbols)

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val number = originalText.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)
        val formattedText = decimalFormat.format(number)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                var digitCount = 0
                for (i in formattedText.indices) {
                    if (formattedText[i].isDigit()) {
                        digitCount++
                        if (digitCount == offset) {
                            return i + 1
                        }
                    }
                }
                return formattedText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val actualOffset = offset.coerceAtMost(formattedText.length)
                return formattedText.substring(0, actualOffset).count { it.isDigit() }
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
