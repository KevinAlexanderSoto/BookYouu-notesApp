package com.kalex.bookyouu_notesapp.subject

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.common.decodeUri
import com.kalex.bookyouu_notesapp.common.handleViewModelState
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.SubjectListViewModel
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui.SubjectListScreen

@Composable
fun SubjectMainScreen(
    onAddNewSubject: () -> Unit,
    onSubjectClickAction: (String) -> Unit,
    subjectViewModel: SubjectListViewModel = hiltViewModel(),
) {
    var onDeleteSubject by remember {
        mutableStateOf(false)
    }
    var subjectToDelete by remember {
        mutableStateOf(-1)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        subjectViewModel.getSubjectList()
    }

    if (onDeleteSubject && subjectToDelete != -1) {
        AlertDialog(
            title = { Text(text = stringResource(id = R.string.subject_delete_title_dialog)) },
            text = {
                Text(
                    fontSize = 20.sp,
                    text = stringResource(id = R.string.subject_delete_text_dialog),
                )
            },
            onDismissRequest = { onDeleteSubject = false },
            confirmButton = {
                TextButton(onClick = {
                    subjectViewModel.getNotesUrl(subjectToDelete)
                    handleViewModelState(
                        subjectViewModel.getNotesUrlState,
                        scope = scope,
                        onEmpty = {
                            subjectViewModel.deleteSubject(subjectId = subjectToDelete)
                        },
                        onLoading = { },
                        onSuccess = { response ->
                            response.forEach { url ->
                                context.contentResolver.delete(
                                    url.decodeUri(),
                                    null,
                                    null,
                                )
                            }
                            subjectViewModel.deleteSubject(subjectId = subjectToDelete)
                        },
                        onError = {
                            TODO()
                        },
                    )
                    onDeleteSubject = false
                }) {
                    Text(
                        text = stringResource(id = R.string.subject_delete_button_text_dialog),
                        fontSize = 18.sp,
                    )
                }
            },
        )
    }

    handleViewModelState(
        subjectViewModel.getSubjectState.collectAsStateWithLifecycle(),
        onEmpty = {
            EmptyScreen(
                onAddItemClick = { onAddNewSubject.invoke() },
                rationaleText = R.string.subject_list_no_subjectsFount_text,
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
                onSubjectLongClickAction = { id ->
                    subjectToDelete = id
                    onDeleteSubject = true
                },
            )
        },
        onError = {
            TODO()
        },
    )
}
