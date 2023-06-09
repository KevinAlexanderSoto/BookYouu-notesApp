package com.kalex.bookyouu_notesapp.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

object BYTextInput {
    @Composable
    fun OutLinedTextField(
        @StringRes label: Int,
        keyboardOptions: KeyboardOptions = KeyboardOptions(
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        isSingleLine: Boolean = true,
        maxLine: Int = 1,
        onTextChange: (String) -> Unit,
    ) {
        var text by remember { mutableStateOf("") }
        onTextChange(text)
        OutlinedTextField(
            value = text,
            singleLine = isSingleLine,
            maxLines = maxLine,
            label = { Text(text = stringResource(label)) },
            onValueChange = {
                if (it.length < 120) {
                    text = it
                }
            },
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(15.dp),
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OutLinedButtonTextField(
        @StringRes label: Int,
        onClick: () -> Unit,
        updateTextValue: () -> String,
    ) {
        var text by remember { mutableStateOf("") }
        text = updateTextValue()
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text(text = stringResource(label)) },
            singleLine = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth(0.9f).clickable { onClick() },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                // For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}
