package com.kalex.bookyouu_notesapp.records.recordList

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun RecordItem(
    recordUri: Uri,
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(360.dp)
            .fillMaxWidth()
            .clickable { },
    ) {
        Image(
            painter = rememberAsyncImagePainter(recordUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .padding(4.dp),
        )
    }
}
