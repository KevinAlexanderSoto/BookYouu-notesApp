package com.kalex.bookyouu_notesapp.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun BYOutLineTextInputField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    onTextChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    onTextChange(text)
    OutlinedTextField(
        value = text,
        label = { Text(text = stringResource(label)) },
        onValueChange = {
            text = it
        },
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(0.9f),
    )
}
