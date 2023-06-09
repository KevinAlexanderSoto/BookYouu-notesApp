package com.kalex.bookyouu_notesapp.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable

@Composable
fun PermissionNotAvailable(
    context: Context,
    contentText: String,
    titleText: String,
    buttonText: String,
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            androidx.compose.material3.Text(text = titleText)
        },
        text = {
            androidx.compose.material3.Text(contentText)
        },
        confirmButton = {
            androidx.compose.material3.Button(onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    },
                )
            }) {
                androidx.compose.material3.Text(buttonText)
            }
        },
    )
}
