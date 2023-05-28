package com.kalex.bookyouu_notesapp.subject

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.subject.createSubject.ViewModelState
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui.SubjectListScreen
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.EmptyScreen

@Composable
fun SubjectMainScreen(
    onAddNewSubject: () -> Unit,
    onSubjectClickAction: (String) -> Unit,
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    subjectViewModel.getSubjectList()
    when (val response = subjectViewModel.getSubjectState.collectAsStateWithLifecycle().value) {
        is ViewModelState.Empty -> {
            EmptyScreen(
                onAddItemClick = { onAddNewSubject.invoke() },
                rationaleText = R.string.subjectList_no_subjectsFount_text,
            )
        }

        is ViewModelState.Error -> TODO()
        is ViewModelState.Loading -> BYLoadingIndicator()
        is ViewModelState.Success -> {
            SubjectListScreen(
                response.data,
                onSubjectClickAction = {
                    onSubjectClickAction(it.toString())
                },
                onAddSubjectClickAction = { onAddNewSubject.invoke() },
            )
        }
    }
}
