package com.kalex.bookyouu_notesapp.records.recordList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kalex.bookyouu_notesapp.common.decodeUri
import com.kalex.bookyouu_notesapp.db.data.Note

@Composable
fun RecordsList(data: List<Note>, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
    ) {
        items(data.size) {
            RecordItem(
                data[it].imgUrl.decodeUri(),
            )
        }
    }
}
