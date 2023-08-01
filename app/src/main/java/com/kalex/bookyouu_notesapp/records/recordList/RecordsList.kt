package com.kalex.bookyouu_notesapp.records.recordList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.db.data.Note
/**
 * basic record list, this was the first impl of the list, latter I migrated to an paginated list
 * **/
@Composable
fun RecordsList(
    data: List<Note>,
    paddingValues: PaddingValues,
    onRecordClick: (Int) -> Unit,
    onDeleteRecord: (Note) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(paddingValues),
    ) {
        items(
            data.size,
            key = { data[it].noteId },
        ) {
            RecordItem(
                data[it].imgUrl.decodeUri(),
                voiceUri = data[it].voiceUri,
                recordDescription = data[it].noteDescription,
                onRecordClick = { onRecordClick(data[it].noteId) },
                onDeleteRecord = {
                    onDeleteRecord(data[it])
                },
            )
        }
    }
}
