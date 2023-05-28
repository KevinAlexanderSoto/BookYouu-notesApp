package com.kalex.bookyouu_notesapp.records

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.records.presentation.RecordsViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.ViewModelState
import com.kalex.bookyouu_notesapp.ui.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.ui.composables.EmptyScreen

@Composable
fun RecordsMainScreen(
    subjectId: String,
    onAddNewRecord: () -> Unit,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
) {
    recordsViewModel.getRecordsList(subjectId)
    when (val response = recordsViewModel.getRecordsState.collectAsStateWithLifecycle().value) {
        is ViewModelState.Empty -> {
            EmptyScreen(
                onAddItemClick = { onAddNewRecord.invoke() },
                rationaleText = R.string.subjectList_no_subjectsFount_text,
            )
        }

        is ViewModelState.Error -> TODO()
        is ViewModelState.Loading -> BYLoadingIndicator()
        is ViewModelState.Success -> {
            /*RecordsListScreen(
                response.data,
                onSubjectClickAction = {
                    onSubjectClickAction(it.toString())
                },
                onAddSubjectClickAction = { onAddNewSubject.invoke() },
            )*/
        }
    }
}

