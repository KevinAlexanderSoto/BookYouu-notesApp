package com.kalex.bookyouu_notesapp.subject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.common.handleViewModelState
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui.SubjectListScreen

@Composable
fun SubjectMainScreen(
    onAddNewSubject: () -> Unit,
    onSubjectClickAction: (String) -> Unit,
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        subjectViewModel.getSubjectList()
    }

    handleViewModelState(
        subjectViewModel.getSubjectState.collectAsStateWithLifecycle(),
        onEmpty = {
            EmptyScreen(
                onAddItemClick = { onAddNewSubject.invoke() },
                rationaleText = R.string.subjectList_no_subjectsFount_text,
            )
        },
        onLoading = { BYLoadingIndicator() },
        onSuccess = { response ->
            SubjectListScreen(
                response,
                onSubjectClickAction = {
                    onSubjectClickAction(it.toString())
                },
                onAddSubjectClickAction = { onAddNewSubject.invoke() },
            )
        },
        onError = {
            TODO()
        },
    )
}
