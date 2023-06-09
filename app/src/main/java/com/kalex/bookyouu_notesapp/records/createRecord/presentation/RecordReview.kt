package com.kalex.bookyouu_notesapp.records.createRecord.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYTextInput

@Composable
fun RecordReview(
    subjectId: String,
    captureUri: Uri,
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = rememberAsyncImagePainter(captureUri),
            contentDescription = null,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth().height(480.dp),
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
            Button(onClick = {
                // TODO: Implement this whit a viewModel
                context.contentResolver.delete(captureUri, null, null)
            }) {
                Text(text = "Delete img")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "add")
            }
        }
    }
}
