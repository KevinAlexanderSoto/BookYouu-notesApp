package com.kalex.bookyouu_notesapp.records.recordsDetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.records.RecordsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordMainDetail(
    recordID: Int,
    paddingValues: PaddingValues,
    recordsViewModel: RecordsViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        recordsViewModel.getRecordById(recordId = recordID)
    }

    handleViewModelState(
        recordsViewModel.getRecordState.collectAsStateWithLifecycle(),
        onEmpty = {
            EmptyScreen(
                title = UiText.StringResource(R.string.records_empty_title),
                description = UiText.StringResource(R.string.records_empty_description),
                mainIcon = R.drawable.octagon_help_svgrepo_com,
                buttonText = UiText.StringResource(R.string.success_screen_buttom_text),
                onButtonClick = { }
            )
        },
        onLoading = { BYLoadingIndicator() },
        onSuccess = { response: Note ->
            RecordDetail(response.imgUrl, response.noteDate, paddingValues)
        },
        onError = {
            TODO()
        },
    )
}
