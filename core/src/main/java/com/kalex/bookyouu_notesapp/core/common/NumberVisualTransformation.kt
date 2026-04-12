package com.kalex.bookyouu_notesapp.core.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class NumberVisualTransformation : VisualTransformation {
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
