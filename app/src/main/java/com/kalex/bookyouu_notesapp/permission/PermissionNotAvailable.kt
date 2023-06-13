package com.kalex.bookyouu_notesapp.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionNotAvailable(
    onConfirmClick: () -> Unit,
    contentText: String,
    titleText: String,
    buttonText: String,
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(text = titleText)
        },
        text = {
            Text(contentText)
        },
        confirmButton = {
            Button(onClick = {
                onConfirmClick()
            }) {
                Text(buttonText)
            }
        },
    )
}
