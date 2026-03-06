package com.kalex.bookyouu_notesapp.records.recordList

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kalex.bookyouu_notesapp.R
import java.io.IOException

@Composable
fun RecordItem(
    recordUri: Uri,
    voiceUri: String,
    recordDescription: String,
    onRecordClick: () -> Unit,
    onDeleteRecord: () -> Unit,
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    AnimatedVisibility(visible = showDeleteDialog) {
        AlertDialog(
            title = { Text(text = stringResource(id = R.string.record_delete_title)) },
            text = {
                Text(
                    text = stringResource(id = R.string.record_delete_text),
                )
            },
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteRecord()
                    showDeleteDialog = false
                }) {
                    Text(text = stringResource(id = R.string.record_confirm_button_text))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(id = R.string.record_cancel_button_text))
                }
            },
        )
    }

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(18.dp)
            .wrapContentSize(),
    ) {
        var textExpanded by remember {
            mutableStateOf(false)
        }
        var isVoicePlaying by remember {
            mutableStateOf(false)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { showDeleteDialog = true },
            ) {
                Icon(Icons.Default.Delete, contentDescription = "delete")
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, recordUri)
                        type = "image/*"
                    }

                    val chooser = Intent.createChooser(intent, "Share image").apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    context.startActivity(chooser)
                },
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
            AsyncImage(
                model = recordUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onRecordClick() },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = recordDescription,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    )
                    .clickable { textExpanded = !textExpanded },
                maxLines = if (textExpanded) Int.MAX_VALUE else 2,
                textAlign = TextAlign.Start,
            )
            IconButton(onClick = {
                val mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer.setDataSource(voiceUri)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    isVoicePlaying = true
                    mediaPlayer.setOnCompletionListener {
                        isVoicePlaying = false
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "play voice",
                    tint = if (isVoicePlaying) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Gray,
                )
            }
        }
    }
}