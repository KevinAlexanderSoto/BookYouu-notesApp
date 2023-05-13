package com.kalex.bookyouu_notesapp.subjectList.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.ui.composables.BookYouuLoadingIndicator

@Composable
fun SubjectMainScreen(
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    subjectViewModel.getSubjectList()
    val state = subjectViewModel.getSubjectState.collectAsState()
    if (state.value.isLoading) {
        BookYouuLoadingIndicator()
    }
    if (!state.value.isLoading && state.value.response == null) {
        EmptySubjectScreen() {
        }
    }
}
