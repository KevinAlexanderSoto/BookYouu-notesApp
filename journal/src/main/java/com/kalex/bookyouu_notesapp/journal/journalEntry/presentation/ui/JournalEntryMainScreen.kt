package com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.ui

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.journal.R
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.JournalEntryViewModel
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.PaginationState
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.PagingJournalEntryViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JournalEntryMainScreen(
    paddingValues: PaddingValues,
    journalId: Int,
    onAddNewEntry: () -> Unit,
    onEntryDetail: (id: Int) -> Unit,
    entryViewModel: JournalEntryViewModel = koinViewModel(),
    pagingViewModel: PagingJournalEntryViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val entriesList = pagingViewModel.entriesList.collectAsStateWithLifecycle()
    val pagingState = pagingViewModel.pagingState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        pagingViewModel.clearPaging()
        pagingViewModel.getJournalEntries(journalId)
        lazyColumnListState.scrollToItem(0, 0)
    }
    
    val shouldPaginate = remember {
        derivedStateOf {
            pagingViewModel.canPaginate && (
                    lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: -5
                    ) >= (lazyColumnListState.layoutInfo.totalItemsCount - 3)
        }
    }

    LaunchedEffect(key1 = shouldPaginate.value) {
        if (shouldPaginate.value && pagingState.value == PaginationState.REQUEST_INACTIVE) {
            pagingViewModel.getJournalEntries(journalId)
        }
    }

    LazyColumn(
        state = lazyColumnListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(paddingValues),
    ) {
        entriesList.value.forEach { category ->
            stickyHeader {
                JournalEntryCategoryHeader(category.name)
            }
            items(
                category.items.size,
                key = { category.items[it].entryId },
            ) {
                JournalEntryItem(
                    entryUri = category.items[it].imgUrl.decodeUri(),
                    voiceUri = category.items[it].voiceUri,
                    description = category.items[it].entryDescription,
                    onEntryClick = { onEntryDetail(category.items[it].entryId) },
                    onDeleteEntry = {
                        entryViewModel.deleteJournalEntry(category.items[it])
                        coroutineScope.launch {
                            context.contentResolver.delete(
                                category.items[it].imgUrl.decodeUri(),
                                null,
                                null,
                            )
                            if (category.items[it].voiceUri.isNotEmpty()) {
                                File(category.items[it].voiceUri).delete()
                            }

                            pagingViewModel.clearPaging()
                            pagingViewModel.getJournalEntries(journalId)
                        }
                        handleViewModelState(
                            collectAsState = entryViewModel.deleteEntryState,
                            scope = coroutineScope,
                            onEmpty = {},
                            onLoading = { },
                            onSuccess = {
                            },
                            onError = {
                                // TODO: Handle error
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

            PaginationState.ERROR -> {
                // TODO: Handle error
            }
            
            PaginationState.PAGINATION_EXHAUST -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(8.dp),
                    ) {
                        Text(text = stringResource(id = R.string.journal_entry_list_end_text))
                    }
                }
            }

            PaginationState.EMPTY -> {
                item {
                    EmptyScreen(
                        title = UiText.StringResource(R.string.journal_entries_empty_title),
                        description = UiText.StringResource(R.string.journal_entries_empty_description),
                        mainIcon = R.drawable.camera_svgrepo_com,
                        buttonText = UiText.StringResource(R.string.journal_list_no_journalsFount_ButtonText),
                        onButtonClick = { onAddNewEntry.invoke() }
                    )
                }
            }
        }
    }
}
