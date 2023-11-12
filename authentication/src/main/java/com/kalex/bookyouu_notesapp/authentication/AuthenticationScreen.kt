package com.kalex.bookyouu_notesapp.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FingerPrintBaseScreen(
    onAuthenticateButtonClick : () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Inicia session con tu huella")
        Button(onClick = {
            onAuthenticateButtonClick()
        }) {
            Text(text = "Iniciar seccion")
        }
    }
}