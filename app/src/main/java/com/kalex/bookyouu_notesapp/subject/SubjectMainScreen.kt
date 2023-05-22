package com.kalex.bookyouu_notesapp.subject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.ViewModelState
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui.EmptySubjectScreen
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui.SubjectListScreen
import com.kalex.bookyouu_notesapp.ui.composables.BYLoadingIndicator

@Composable
fun SubjectMainScreen(
    onAddNewSubject: () -> Unit,
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    subjectViewModel.getSubjectList()
    when (val response = subjectViewModel.getSubjectState.collectAsState().value) {
        is ViewModelState.Empty -> {
            EmptySubjectScreen(
                onCreateSubjectClick = { onAddNewSubject.invoke() },
            )
        }

        is ViewModelState.Error -> TODO()
        is ViewModelState.Loading -> BYLoadingIndicator()
        is ViewModelState.Success -> {
            SubjectListScreen(
                response.data,
                onSubjectClickAction = {
                    // todo: navigate to detail
                },
                onAddSubjectClickAction = { onAddNewSubject.invoke() },
            )
        }
    }
}
