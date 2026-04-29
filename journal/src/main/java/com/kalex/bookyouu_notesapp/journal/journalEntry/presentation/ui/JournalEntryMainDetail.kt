package com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.journal.R
import com.kalex.bookyouu_notesapp.core.common.UiText
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.composables.EmptyScreen
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.db.data.JournalEntry
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.JournalEntryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun JournalEntryMainDetail(
    entryId: Int,
    paddingValues: PaddingValues,
    entryViewModel: JournalEntryViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        entryViewModel.getEntryById(entryId = entryId)
    }

    handleViewModelState(
        entryViewModel.getEntryDetailState.collectAsStateWithLifecycle(),
        onEmpty = {
            EmptyScreen(
                title = UiText.StringResource(R.string.journal_entries_empty_title),
                description = UiText.StringResource(R.string.journal_entries_empty_description),
                mainIcon = com.kalex.bookyouu_notesapp.core.R.drawable.file_add_svgrepo_com,
                buttonText = UiText.StringResource(R.string.success_screen_buttom_text),
                onButtonClick = { }
            )
        },
        onLoading = { BYLoadingIndicator() },
        onSuccess = { response: JournalEntry ->
            JournalEntryDetail(response.imgUrl, response.entryDate, paddingValues)
        },
        onError = {
            // TODO: Handle error
        },
    )
}
