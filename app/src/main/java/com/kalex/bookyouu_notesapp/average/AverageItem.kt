package com.kalex.bookyouu_notesapp.average

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.core.common.composables.BYTextInput

@Composable
fun AverageItem(
    itemNumber: Int,
    onPercentageChange: (String) -> Unit,
    onRatingChange: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Text(text = "$itemNumber", modifier = Modifier.padding(4.dp, 0.dp))
        Column(
            modifier = Modifier.fillMaxWidth(0.75f),
        ) {
            BYTextInput.OutLinedTextField(
                label = R.string.average_item_ratings_label,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                ),
                onTextChange = {
                    onRatingChange(it)
                },
            )
        }
        BYTextInput.OutLinedTextField(
            label = R.string.average_item_percentage_label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
            ),
            onTextChange = { onPercentageChange(it) },
        )
    }
}
