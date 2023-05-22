package com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.db.data.Subject

@Composable
fun SubjectListScreen(
    subjectList: List<Subject>,
    onAddSubjectClickAction: () -> Unit,
    onSubjectClickAction: (subjectId: Int) -> Unit,
) {
    Column() {
        Box(
            Modifier.padding(top = 20.dp).fillMaxWidth(),
        ) {
            Text(
                text = "Tus clases",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
            )
            IconButton(
                onClick = { onAddSubjectClickAction() },
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 20.dp),

            ) {
                Icon(Icons.Default.Add, contentDescription = "add new")
            }
        }
        LazyColumn {
            items(subjectList.size) {
                with(subjectList[it]) {
                    SubjectItem(
                        title = subjectName,
                        subTitle = subjectDay.toString(),
                        classRoom = classroom,
                        onSubjectItemClick = {
                        },
                    )
                }
            }
        }
    }
}
