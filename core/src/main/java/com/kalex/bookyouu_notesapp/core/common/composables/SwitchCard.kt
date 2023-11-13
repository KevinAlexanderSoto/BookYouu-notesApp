package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SwitchCard(
    initialState: Boolean,
    switchText: String,
    onChecked: (Boolean) -> Unit
) {
    Card(modifier = Modifier.wrapContentSize()) {
        var checked by remember { mutableStateOf(initialState) }
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = switchText)
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onChecked(it)
                }
            )
        }
    }
}

@Preview
@Composable
fun SwitchCardPreview() {
    /*SwitchCard(switchText = "Activar feature", {

    })*/
}