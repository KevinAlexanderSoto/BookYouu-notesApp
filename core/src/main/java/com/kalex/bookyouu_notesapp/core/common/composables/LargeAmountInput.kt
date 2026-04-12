package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.input.VisualTransformation
import com.kalex.bookyouu_notesapp.core.common.NumberVisualTransformation

@Composable
fun LargeAmountInput(
    label: String,
    amount: String,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    currencySymbol: String = "$"
) {
    val visualTransformation = remember { NumberVisualTransformation() }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = currencySymbol,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
            
            TextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() }) onAmountChange(it) },
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                placeholder = {
                    Text(
                        text = "0.00",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f),
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = visualTransformation,
                singleLine = true,
                modifier = Modifier.width(IntrinsicSize.Min)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LargeAmountInputPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            LargeAmountInput(
                label = "AMOUNT",
                amount = "1250",
                onAmountChange = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun LargeAmountInputEmptyPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            LargeAmountInput(
                label = "AMOUNT",
                amount = "",
                onAmountChange = {}
            )
        }
    }
}
