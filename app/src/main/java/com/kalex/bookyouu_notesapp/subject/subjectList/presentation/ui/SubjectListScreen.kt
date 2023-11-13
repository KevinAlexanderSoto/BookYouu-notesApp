@file:OptIn(ExperimentalFoundationApi::class)

package com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.ads.AdmobBanner
import com.kalex.bookyouu_notesapp.ads.AdsUniqueIds
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.subject.createSubject.DayOfWeekStringFactory

@Composable
fun SubjectListScreen(
    subjectList: List<Subject>,
    onAddSubjectClickAction: () -> Unit,
    onSubjectClickAction: (subjectId: Int) -> Unit,
    onSubjectLongClickAction: (subjectId: Int) -> Unit,
) {
    Column() {
        Box(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.subject_list_title),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
            )
            IconButton(
                onClick = { onAddSubjectClickAction() },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),

            ) {
                Icon(Icons.Default.Add, contentDescription = "add new")
            }
        }
        AdmobBanner(
            modifier = Modifier.fillMaxWidth(),
            AdsUniqueIds.SubjectMainTop
        )
        LazyColumn {
            items(subjectList.size, key = { subjectList[it].subjectId }) {
                Row(modifier = Modifier.animateItemPlacement(tween(durationMillis = 250))) {
                    with(subjectList[it]) {
                        SubjectItem(
                            title = subjectName,
                            subTitle = subjectDay.map { dayOfWeek ->
                                stringResource(
                                    id = DayOfWeekStringFactory.getDayStringResource(
                                        dayOfWeek,
                                    ),
                                )
                            }.toString().removePrefix("[").removeSuffix("]"),
                            classRoom = classroom,
                            onSubjectItemClick = {
                                onSubjectClickAction(subjectId)
                            },
                            onSubjectLongItemClick = {
                                onSubjectLongClickAction(subjectId)
                            }
                        )
                    }
                }
            }
        }
    }
}
