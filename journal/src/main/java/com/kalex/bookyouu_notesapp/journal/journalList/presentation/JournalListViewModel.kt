package com.kalex.bookyouu_notesapp.journal.journalList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import com.kalex.bookyouu_notesapp.db.data.Journal
import com.kalex.bookyouu_notesapp.journal.data.JournalEntryRepository
import com.kalex.bookyouu_notesapp.journal.data.JournalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JournalListViewModel(
    private val journalRepository: JournalRepository,
    private val journalEntryRepository: JournalEntryRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _getJournalState =
        MutableStateFlow<ViewModelState<List<Journal>>>(ViewModelState.Loading(true))
    val getJournalState = _getJournalState.asStateFlow()

    private val _getJournalEntriesUrlState =
        MutableStateFlow<ViewModelState<List<String>>>(ViewModelState.Loading(false))
    val getJournalEntriesUrlState = _getJournalEntriesUrlState.asStateFlow()

    fun getJournalList() {
        viewModelScope.launch(dispatcher) {
            try {
                journalRepository.getJournalList().collectLatest { journals ->
                    if (journals.isEmpty()) {
                        _getJournalState.update {
                            ViewModelState.Empty()
                        }
                    } else {
                        _getJournalState.update {
                            ViewModelState.Success(journals)
                        }
                    }
                }
            } catch (e: Exception) {
                _getJournalState.update {
                    ViewModelState.Error(e)
                }
            }
        }
    }

    fun getJournalEntriesUrl(journalId: Int) {
        viewModelScope.launch(dispatcher) {
            journalEntryRepository.getJournalEntriesUriOrderByDate(journalId).collectLatest { listEntryUrl ->
                try {
                    if (listEntryUrl.isEmpty()) {
                        _getJournalEntriesUrlState.update {
                            ViewModelState.Empty()
                        }
                    } else {
                        _getJournalEntriesUrlState.update {
                            ViewModelState.Success(listEntryUrl)
                        }
                    }
                } catch (e: Exception) {
                    _getJournalState.update {
                        ViewModelState.Error(e)
                    }
                }
            }
        }
    }

    fun deleteJournal(journalId: Int) {
        viewModelScope.launch(dispatcher) {
            journalEntryRepository.deleteJournalEntriesByJournalId(journalId)
            journalRepository.deleteJournal(journalId)
        }
    }
}
