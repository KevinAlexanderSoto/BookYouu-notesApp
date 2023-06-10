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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYTextInput
import com.kalex.bookyouu_notesapp.common.encodeUri
import com.kalex.bookyouu_notesapp.records.RecordsViewModel
import kotlinx.coroutines.launch

@Composable
fun RecordReview(
    subjectId: Int?,
    captureUri: Uri,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
    onReCapture: () -> Unit,
) {
    val context = LocalContext.current
    val description = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        ElevatedCard(
            modifier = Modifier.padding(8.dp).wrapContentSize(),
        ) {
            Image(
                painter = rememberAsyncImagePainter(captureUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .padding(4.dp),
            )
        }
        BYTextInput.OutLinedTextField(
            label = R.string.record_confirmation_description_label,
            isSingleLine = false,
            maxLine = 3,
        ) {
            description.value = it
        }
        Text(text = "Save the note", textAlign = TextAlign.Center)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 10.dp),
        ) {
            FloatingActionButton(onClick = {
                scope.launch {
                    context.contentResolver.delete(
                        captureUri,
                        null,
                        null,
                    )
                }.invokeOnCompletion {
                    onReCapture()
                }
            }) {
                Icon(Icons.Default.Close, contentDescription = "negative")
            }

            FloatingActionButton(onClick = {
                recordsViewModel.createRecord(
                    subjectId = subjectId ?: 0,
                    imgUrl = captureUri.toString().encodeUri(),
                    noteDescription = description.value,
                )
            }) {
                Icon(Icons.Default.Check, contentDescription = "positive")
            }
        }
    }
}
