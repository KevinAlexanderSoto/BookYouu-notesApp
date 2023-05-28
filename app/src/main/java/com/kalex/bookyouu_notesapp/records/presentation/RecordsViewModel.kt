package com.kalex.bookyouu_notesapp.records.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import com.kalex.bookyouu_notesapp.subject.createSubject.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val notesRepositoryImpl: NotesRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _getRecordsState =
        MutableStateFlow<ViewModelState<List<Note>>>(ViewModelState.Loading(true))
    val getRecordsState: StateFlow<ViewModelState<List<Note>>>
        get() = _getRecordsState.asStateFlow()

    fun getRecordsList(subjectId: String) {
        viewModelScope.launch(dispatcher){
            try {
                _getRecordsState.update { ViewModelState.Loading(true) }
                notesRepositoryImpl.getNotesByDate(subjectId).collectLatest { list ->
                    if (list.isEmpty()) {
                        _getRecordsState.update { ViewModelState.Empty() }
                    } else {
                        _getRecordsState.update { ViewModelState.Success(list) }
                    }
                }
            } catch (e: Exception) {
                _getRecordsState.update { ViewModelState.Error(e) }
            }
        }
    }
}
