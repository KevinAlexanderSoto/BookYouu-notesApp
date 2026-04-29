package com.kalex.bookyouu_notesapp.journal

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.core.common.decodeUri
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.journal.journalList.presentation.JournalListViewModel
import com.kalex.bookyouu_notesapp.journal.journalList.presentation.ui.JournalListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun JournalMainScreen(
    onAddNewJournal: () -> Unit,
    onJournalClickAction: (String) -> Unit,
    journalViewModel: JournalListViewModel = koinViewModel(),
) {
    var onDeleteJournal by remember { mutableStateOf(false) }
    var journalToDelete by remember { mutableStateOf(-1) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        journalViewModel.getJournalList()
    }

    if (onDeleteJournal && journalToDelete != -1) {
        AlertDialog(
            title = { Text(text = stringResource(id = R.string.journal_delete_title_dialog)) },
            text = {
                Text(
                    fontSize = 20.sp,
                    text = stringResource(id = R.string.journal_delete_text_dialog),
                )
            },
            onDismissRequest = { onDeleteJournal = false },
            confirmButton = {
                TextButton(onClick = {
                    journalViewModel.getJournalEntriesUrl(journalToDelete)
                    handleViewModelState(
                        journalViewModel.getJournalEntriesUrlState,
                        scope = scope,
                        onEmpty = {
                            journalViewModel.deleteJournal(journalId = journalToDelete)
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
                            journalViewModel.deleteJournal(journalId = journalToDelete)
                        },
                        onError = {
                            // TODO: Handle error
                        },
                    )
                    onDeleteJournal = false
                }) {
                    Text(
                        text = stringResource(id = R.string.journal_delete_button_text_dialog),
                        fontSize = 18.sp,
                    )
                }
            },
        )
    }

    handleViewModelState(
        journalViewModel.getJournalState.collectAsStateWithLifecycle(),
        onEmpty = {
            EmptyScreen(
                title = UiText.StringResource(R.string.journal_empty_title),
                description = UiText.StringResource(R.string.journal_empty_description),
                mainIcon = com.kalex.bookyouu_notesapp.core.R.drawable.file_add_svgrepo_com,
                buttonText = UiText.StringResource(R.string.journal_list_no_journalsFount_ButtonText),
                onButtonClick = { onAddNewJournal.invoke() }
            )
        },
        onLoading = { BYLoadingIndicator() },
        onSuccess = { response ->
            JournalListScreen(
                response,
                onJournalClickAction = {
                    onJournalClickAction(it.toString())
                },
                onAddJournalClickAction = { onAddNewJournal.invoke() },
                onJournalLongClickAction = { id ->
                    journalToDelete = id
                    onDeleteJournal = true
                },
            )
        },
        onError = {
            // TODO: Handle error
        },
    )
}
