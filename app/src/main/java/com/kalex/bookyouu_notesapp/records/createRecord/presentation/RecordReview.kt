package com.kalex.bookyouu_notesapp.records.createRecord.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYTextInput

@Composable
fun RecordReview(
    subjectId: String,
    captureUri: Uri,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = rememberAsyncImagePainter(captureUri),
            contentDescription = null,
        )

        BYTextInput.OutLinedTextField(
            label = R.string.record_confirmation_description_label,
            isSingleLine = false,
            maxLine = 3,
            ) {

        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(0.9f),
        ) {
            Button(onClick = { /*TODO*/ }) {
//
            }
            Button(onClick = { /*TODO*/ }) {
            }
        }
    }
}
