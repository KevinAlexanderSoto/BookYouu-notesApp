package com.kalex.bookyouu_notesapp.records.recordList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.db.data.Note
/**
 * basic record list, this was the first impl of the list, latter I migrated to an paginated list
 * **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordsList(
    data: List<Note>,
    paddingValues: PaddingValues,
    onRecordClick: (Int) -> Unit,
    onDeleteRecord: (Note) -> Unit,
) {
    var dataList: List<Category>? = null
    LaunchedEffect(key1 = data, block = {

        val dataMap = data.groupBy {
            it.noteDate
        }.toSortedMap()
        dataList = dataMap.map {
            Category(
                name = it.key.toString(),
                items = it.value
            )
        }
    })
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(paddingValues),
    ) {
        dataList?.forEach { category ->
            stickyHeader {
                CategoryHeader(category.name)
            }
            items(
                category.items.size ,
                key = { category.items[it].noteId },
            ) {
                RecordItem(
                    category.items[it].imgUrl.decodeUri(),
                    voiceUri = category.items[it].voiceUri,
                    recordDescription = category.items[it].noteDescription,
                    onRecordClick = { onRecordClick(category.items[it].noteId) },
                    onDeleteRecord = {
                        onDeleteRecord(category.items[it])
                    },
                )
            }
        }

    }
}
@Composable
fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
    )
}

data class Category(
    val name: String,
    val items: List<Note>
)