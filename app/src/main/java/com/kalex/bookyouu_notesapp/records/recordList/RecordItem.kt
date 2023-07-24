package com.kalex.bookyouu_notesapp.records.recordList

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

                    val chooser = Intent.createChooser(intent, "Share image")

                    context.startActivity(chooser)
                },
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
            AsyncImage(
                model = recordUri,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
                    .height(280.dp)
                    .clip(
                        RoundedCornerShape(14.dp),
                    )
                    .clickable { onRecordClick() },
            )
        }
        AnimatedVisibility(visible = voiceUri.isNotEmpty()) {
            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    MediaPlayer().apply {
                        try {
                            setDataSource(voiceUri)
                            prepare()
                            start()
                        } catch (e: IOException) {
                        }
                    }
                },
            ) {
                Icon(Icons.Default.Delete, contentDescription = "delete")
            }
        }

        Text(
            text = recordDescription,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp, 10.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                ).clickable {
                    textExpanded = !textExpanded
                },
            maxLines = if (!textExpanded) 1 else 3,
        )
    }
}
