package com.kalex.bookyouu_notesapp.records

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.common.decodeUri
import com.kalex.bookyouu_notesapp.common.handleViewModelState
import com.kalex.bookyouu_notesapp.permission.RequireCameraPermission
import com.kalex.bookyouu_notesapp.records.recordList.RecordItem
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordsMainScreen(
    paddingValues: PaddingValues,
    subjectId: Int,
    onAddNewRecord: () -> Unit,
    onRecordDetail: (id: Int) -> Unit,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
    pagingRecordsViewModel: PagingRecordsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    RequireCameraPermission(
        permission = recordsViewModel.permissionsList,
        onPermissionDenied = {
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                },
            )
        },
    ) {
        val lazyColumnListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        val noteList = pagingRecordsViewModel.notesList.collectAsStateWithLifecycle()
        val pagingState = pagingRecordsViewModel.pagingState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            pagingRecordsViewModel.clearPaging()
            pagingRecordsViewModel.getNotes(subjectId)
            lazyColumnListState.scrollToItem(0, 0)
        }
        val shouldPaginate = remember {
            derivedStateOf {
                pagingRecordsViewModel.canPaginate && (
                    lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: -5
                    ) >= (lazyColumnListState.layoutInfo.totalItemsCount - 3)
            }
        }

        LaunchedEffect(key1 = shouldPaginate.value) {
            if (shouldPaginate.value && pagingState.value == PaginationState.REQUEST_INACTIVE) {
                pagingRecordsViewModel.getNotes(subjectId)
            }
        }

        LazyColumn(
            state = lazyColumnListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(paddingValues),
        ) {
            items(
                noteList.value.size,
                key = { noteList.value[it].noteId },
            ) {
                RecordItem(
                    noteList.value[it].imgUrl.decodeUri(),
                    voiceUri = noteList.value[it].voiceUri,
                    recordDescription = noteList.value[it].noteDescription,
                    onRecordClick = { onRecordDetail(noteList.value[it].noteId) },
                    onDeleteRecord = {
                        recordsViewModel.deleteRecord(noteList.value[it])
                        coroutineScope.launch {
                            context.contentResolver.delete(
                                noteList.value[it].imgUrl.decodeUri(),
                                null,
                                null,
                            )
                            if (noteList.value[it].voiceUri.isNotEmpty()) {
                                File(noteList.value[it].voiceUri).delete()
                            }

                            pagingRecordsViewModel.clearPaging()
                            pagingRecordsViewModel.getNotes(subjectId)
                        }
                        handleViewModelState(
                            collectAsState = recordsViewModel.deleteRecordsState,
                            scope = coroutineScope,
                            onEmpty = {},
                            onLoading = { },
                            onSuccess = {
                            },
                            onError = {
                                TODO()
                            },
                        )
                    },
                )
            }
            when (pagingState.value) {
                PaginationState.REQUEST_INACTIVE -> {
                }

                PaginationState.LOADING -> {
                    item {
                        BYLoadingIndicator()
                    }
                }

                PaginationState.PAGINATING -> {
                    item {
                        BYLoadingIndicator()
                    }
                }

                PaginationState.ERROR -> TODO()
                PaginationState.PAGINATION_EXHAUST -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxHeight().padding(8.dp),
                        ) {
                            Text(text = stringResource(id = R.string.record_list_end_text))
                        }
                    }
                }

                PaginationState.EMPTY -> {
                    item {
                        EmptyScreen(
                            onAddItemClick = { onAddNewRecord.invoke() },
                            rationaleText = R.string.records_list_no_recordsFount_text,
                        )
                    }
                }
            }
        }
    }
}
