package com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.kalex.bookyouu_notesapp.db.data.Subject

@Composable
fun SubjectListScreen(
    subjectList: List<Subject>,
    onSubjectClickAction: (subjectId: Int) -> Unit,
) {
    LazyColumn {
        items(subjectList.size) {
        }
    }
    // list of subjects, each subject have an click action, and each subject is inside a card
}
