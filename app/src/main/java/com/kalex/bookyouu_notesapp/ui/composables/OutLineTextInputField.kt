package com.kalex.bookyouu_notesapp.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BYOutLineTextInputField(
    label: String,
    onTextChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    onTextChange(text)
    OutlinedTextField(
        value = text,
        label = { Text(text = label) },
        onValueChange = {
            text = it
        },
    )
}
