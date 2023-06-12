package com.kalex.bookyouu_notesapp.records.recordList

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun RecordItem(
    recordUri: Uri,
    recordDescription: String,
    onRecordClick: () -> Unit,
    onDeleteRecord: () -> Unit,
) {
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
                onClick = { onDeleteRecord() },
            ) {
                Icon(Icons.Default.Delete, contentDescription = "delete")
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
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onRecordClick() },
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "view")
            }
        }
        Text(
            text = recordDescription,
            textAlign = TextAlign.Center,
            modifier = Modifier
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
