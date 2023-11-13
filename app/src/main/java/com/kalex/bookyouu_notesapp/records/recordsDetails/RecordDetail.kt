package com.kalex.bookyouu_notesapp.records.recordsDetails

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import com.kalex.bookyouu_notesapp.ads.AdmobBanner
import com.kalex.bookyouu_notesapp.ads.AdsUniqueIds
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun RecordDetail(
    imgUrl: String,
    noteDate: Date,
    paddingValues: PaddingValues,
) {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    var dateText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        val format = SimpleDateFormat("dd/MM/yy")
        dateText = format.format(noteDate)
    }
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        Text(
            text = dateText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        AsyncImage(
            model = imgUrl,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y,
                )
                .transformable(state = state)
                .fillMaxSize(),
        )
        AdmobBanner(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            AdsUniqueIds.RecordsDetailsBottom
        )
    }
}
