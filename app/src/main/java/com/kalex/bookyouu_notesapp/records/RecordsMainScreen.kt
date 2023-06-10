package com.kalex.bookyouu_notesapp.records

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.ViewModelState
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.permission.RequireCameraPermission
import com.kalex.bookyouu_notesapp.records.recordList.RecordsList

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordsMainScreen(
    subjectId: Int,
    onAddNewRecord: () -> Unit,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
) {
    RequireCameraPermission(
        permission = recordsViewModel.permissionsList,
    ) {
        LaunchedEffect(Unit) {
            recordsViewModel.getRecordsList(subjectId)
        }

        when (val response = recordsViewModel.getRecordsState.collectAsStateWithLifecycle().value) {
            is ViewModelState.Empty -> {
                EmptyScreen(
                    onAddItemClick = { onAddNewRecord.invoke() },
                    rationaleText = R.string.subjectList_no_subjectsFount_text, // TODO: Add strings resources
                )
            }

            is ViewModelState.Error -> TODO()
            is ViewModelState.Loading -> BYLoadingIndicator()
            is ViewModelState.Success -> { RecordsList() }
            else -> {
                TODO()
            }
        }
    }
}
