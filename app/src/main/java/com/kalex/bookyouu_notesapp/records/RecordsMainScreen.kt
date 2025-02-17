package com.kalex.bookyouu_notesapp.records

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.records.recordList.CategoryHeader
import com.kalex.bookyouu_notesapp.records.recordList.RecordItem
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
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
        noteList.value.forEach { category ->
            stickyHeader {
                CategoryHeader(category.name)
            }
            items(
                category.items.size,
                key = { category.items[it].noteId },
            ) {
                RecordItem(
                    category.items[it].imgUrl.decodeUri(),
                    voiceUri = category.items[it].voiceUri,
                    recordDescription = category.items[it].noteDescription,
                    onRecordClick = { onRecordDetail(category.items[it].noteId) },
                    onDeleteRecord = {
                        recordsViewModel.deleteRecord(category.items[it])
                        coroutineScope.launch {
                            context.contentResolver.delete(
                                category.items[it].imgUrl.decodeUri(),
                                null,
                                null,
                            )
                            if (category.items[it].voiceUri.isNotEmpty()) {
                                File(category.items[it].voiceUri).delete()
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
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(8.dp),
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
                        addButtonText = stringResource(R.string.subject_list_no_subjectsFount_ButtonText),
                    )
                }
            }
        }
    }
}
