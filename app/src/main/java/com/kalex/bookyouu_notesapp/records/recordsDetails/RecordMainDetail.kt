package com.kalex.bookyouu_notesapp.records.recordsDetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.common.handleViewModelState
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.records.RecordsViewModel

@Composable
fun RecordMainDetail(
    recordID: Int,
    paddingValues: PaddingValues,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        recordsViewModel.getRecordById(recordId = recordID)
    }

    handleViewModelState(
        recordsViewModel.getRecordState.collectAsStateWithLifecycle(),
        onEmpty = {
            EmptyScreen(
                onAddItemClick = { },
                rationaleText = R.string.subjectList_no_subjectsFount_text,
            )
        },
        onLoading = { BYLoadingIndicator() },
        onSuccess = { response: Note ->
            RecordDetail(response.imgUrl, response.noteDate,paddingValues)
        },
        onError = {
            TODO()
        },
    )
}
