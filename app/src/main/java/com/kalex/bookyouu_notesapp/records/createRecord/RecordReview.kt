package com.kalex.bookyouu_notesapp.records.createRecord

import android.net.Uri
import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.BYTextInput
import com.kalex.bookyouu_notesapp.common.handleViewModelState
import com.kalex.bookyouu_notesapp.records.AudioRecordViewModel
import com.kalex.bookyouu_notesapp.records.RecordsViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RecordReview(
    subjectId: Int?,
    captureUri: Uri,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
    audioRecordViewModel: AudioRecordViewModel = hiltViewModel(),
    onReCapture: () -> Unit,
    onCaptureSaved: () -> Unit,
) {
    val loadingState = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val description = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    if (loadingState.value) {
        BYLoadingIndicator()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
            ) {
                AsyncImage(
                    model = captureUri,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(480.dp)
                        .clip(
                            RoundedCornerShape(12.dp),
                        ),
                )
            }
            var isPressed by remember { mutableStateOf(false) }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.98f),
            ) {
                BYTextInput.OutLinedTextField(
                    label = R.string.record_confirmation_description_label,
                    isSingleLine = false,
                    maxLine = 3,
                    textFieldModifier = Modifier.fillMaxWidth(0.85f).padding(8.dp, 0.dp),
                ) {
                    description.value = it
                }
                RecordAudioButton(
                    buttonPressed = {
                        isPressed = true
                        val audioDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "BookYouUAudio")
                        audioDir.mkdirs()
                        val audioDirPath = audioDir.absolutePath

                        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                            .format(System.currentTimeMillis())

                        val recordingFile = File("$audioDirPath/$name.m4a").path
                        audioRecordViewModel.setCurrentPathToSave(recordingFile)
                        audioRecordViewModel.beginAudioRecord()
                    },
                    buttonReleased = {
                        isPressed = false
                        if (audioRecordViewModel.canStopRecord.value) {
                            audioRecordViewModel.stopRecording()
                        }
                    },
                )
            }
            AnimatedVisibility(visible = isPressed) {
                Text(text = "Recording ...")
            }
            Text(
                text = stringResource(id = R.string.record_confirmation_buttons_title),
                textAlign = TextAlign.Center,
            )
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
                    Icon(Icons.Default.Close, contentDescription = "do not capture")
                }
                FloatingActionButton(onClick = {
                    recordsViewModel.createRecord(
                        subjectId = subjectId ?: 0,
                        imgUrl = captureUri.toString(),
                        voiceNoteUri = audioRecordViewModel.getCurrentPath(),
                        noteDescription = description.value,
                    )
                    handleViewModelState(
                        collectAsState = recordsViewModel.saveRecordsState,
                        scope = scope,
                        onLoading = { loadingState.value = true },
                        onSuccess = {
                            onCaptureSaved()
                            loadingState.value = false
                        },
                        onError = {},
                        onEmpty = {},
                    )
                }) {
                    Icon(Icons.Default.Check, contentDescription = "capture")
                }
            }
        }
    }
}

@Composable
fun RecordAudioButton(
    buttonPressed: () -> Unit,
    buttonReleased: () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = isPressed, block = {
        if (isPressed) buttonPressed.invoke() else buttonReleased.invoke()
    })
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isPressed) Color.Black else MaterialTheme.colorScheme.primary)
            .size(50.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        try {
                            isPressed = true
                            awaitRelease()
                        } finally {
                            isPressed = false
                        }
                    },
                )
            },

    ) {
        Icon(Icons.Default.Info, contentDescription = "capture")
    }
}
