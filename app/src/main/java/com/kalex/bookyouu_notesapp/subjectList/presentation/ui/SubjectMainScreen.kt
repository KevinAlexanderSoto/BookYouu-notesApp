package com.kalex.bookyouu_notesapp.subjectList.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.ui.composables.BYLoadingIndicator

@Composable
fun SubjectMainScreen(
    onAddNewSubject: () -> Unit,
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    subjectViewModel.getSubjectList()
    val state = subjectViewModel.getSubjectState.collectAsState()
    if (state.value.isLoading) {
        BYLoadingIndicator()
    }
    if (!state.value.isLoading && state.value.response == null) {
        EmptySubjectScreen(
            onCreateSubjectClick = { onAddNewSubject.invoke() },
        )
    }
}
