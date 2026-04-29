package com.kalex.bookyouu_notesapp.journal.journalEntry.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import com.kalex.bookyouu_notesapp.db.data.JournalEntry
import com.kalex.bookyouu_notesapp.journal.data.JournalEntryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class JournalEntryViewModel(
    private val repository: JournalEntryRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _getEntriesState =
        MutableStateFlow<ViewModelState<List<JournalEntry>>>(ViewModelState.Loading(true))
    val getEntriesState: StateFlow<ViewModelState<List<JournalEntry>>>
        get() = _getEntriesState.asStateFlow()

    fun getEntriesList(journalId: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                _getEntriesState.update { ViewModelState.Loading(true) }
                repository.getJournalEntriesByDate(journalId).collectLatest { list ->
                    if (list.isEmpty()) {
                        _getEntriesState.update { ViewModelState.Empty() }
                    } else {
                        _getEntriesState.update { ViewModelState.Success(list) }
                    }
                }
            } catch (e: Exception) {
                _getEntriesState.update { ViewModelState.Error(e) }
            }
        }
    }

    private val _saveEntryState =
        MutableStateFlow<ViewModelState<Unit>>(ViewModelState.Loading(true))
    val saveEntryState: StateFlow<ViewModelState<Unit>>
        get() = _saveEntryState.asStateFlow()

    fun createJournalEntry(
        journalId: Int,
        imgUrl: String,
        voiceNoteUri: String,
        description: String,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                _saveEntryState.update { ViewModelState.Loading(true) }
                repository.createJournalEntry(
                    JournalEntry(
                        journalId = journalId,
                        imgUrl = imgUrl,
                        entryDate = Date(),
                        voiceUri = voiceNoteUri,
                        entryDescription = description,
                    ),
                )
                _saveEntryState.update { ViewModelState.Success(Unit) }
            } catch (e: Exception) {
                _saveEntryState.update { ViewModelState.Error(e) }
            }
        }
    }

    private val _getEntryDetailState =
        MutableStateFlow<ViewModelState<JournalEntry>>(ViewModelState.Loading(true))
    val getEntryDetailState: StateFlow<ViewModelState<JournalEntry>>
        get() = _getEntryDetailState.asStateFlow()

    fun getEntryById(entryId: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                _getEntryDetailState.update { ViewModelState.Loading(true) }
                repository.getJournalEntryById(entryId).collectLatest { entry ->
                    _getEntryDetailState.update { ViewModelState.Success(entry) }
                }
            } catch (e: Exception) {
                _getEntryDetailState.update { ViewModelState.Error(e) }
            }
        }
    }

    private val _deleteEntryState =
        MutableStateFlow<ViewModelState<Unit>>(ViewModelState.Loading(true))
    val deleteEntryState: StateFlow<ViewModelState<Unit>>
        get() = _deleteEntryState.asStateFlow()

    fun deleteJournalEntry(entry: JournalEntry) {
        viewModelScope.launch(dispatcher) {
            try {
                _deleteEntryState.update { ViewModelState.Loading(true) }
                repository.deleteJournalEntry(entry)
                _deleteEntryState.update { ViewModelState.Success(Unit) }
            } catch (e: Exception) {
                _deleteEntryState.update { ViewModelState.Error(e) }
            }
        }
    }
}
