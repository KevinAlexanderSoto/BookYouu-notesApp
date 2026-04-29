package com.kalex.bookyouu_notesapp.journal.createJournal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import com.kalex.bookyouu_notesapp.db.data.Journal
import com.kalex.bookyouu_notesapp.journal.data.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JournalFormViewModel(
    private val repository: JournalRepository,
) : ViewModel() {
    private val _createJournalState =
        MutableStateFlow<ViewModelState<Boolean>>(
            ViewModelState.Loading(true),
        )
    val createJournalState = _createJournalState.asStateFlow()

    fun createJournal(journal: Journal) {
        viewModelScope.launch {
            try {
                _createJournalState.update {
                    ViewModelState.Loading(true)
                }
                repository.upsertJournal(journal)
                _createJournalState.update {
                    ViewModelState.Success(true)
                }
            } catch (e: Exception) {
                _createJournalState.update {
                    ViewModelState.Error(e)
                }
            }
        }
    }
}
