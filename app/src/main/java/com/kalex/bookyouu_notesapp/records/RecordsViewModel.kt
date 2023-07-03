package com.kalex.bookyouu_notesapp.records

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.common.ViewModelState
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val notesRepositoryImpl: NotesRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val REQUIRED_PERMISSIONS =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    val permissionsList: List<String>
        get() = REQUIRED_PERMISSIONS

    private val _getRecordsState =
        MutableStateFlow<ViewModelState<List<Note>>>(ViewModelState.Loading(true))
    val getRecordsState: StateFlow<ViewModelState<List<Note>>>
        get() = _getRecordsState.asStateFlow()

    fun getRecordsList(subjectId: Int) {
        viewModelScope.launch(dispatcher) {
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

    private val _saveRecordsState =
        MutableStateFlow<ViewModelState<Unit>>(ViewModelState.Loading(true))
    val saveRecordsState: StateFlow<ViewModelState<Unit>>
        get() = _saveRecordsState.asStateFlow()

    fun createRecord(subjectId: Int, imgUrl: String, noteDescription: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _saveRecordsState.update { ViewModelState.Loading(true) }
                notesRepositoryImpl.createNotes(
                    Note(
                        subjectId = subjectId,
                        imgUrl = imgUrl,
                        noteDate = Date(),
                        noteDescription = noteDescription,
                    ),
                )
                _saveRecordsState.update { ViewModelState.Success(Unit) }
            } catch (e: Exception) {
                _saveRecordsState.update { ViewModelState.Error(e) }
            }
        }
    }

    private val _getRecordState =
        MutableStateFlow<ViewModelState<Note>>(ViewModelState.Loading(true))
    val getRecordState: StateFlow<ViewModelState<Note>>
        get() = _getRecordState.asStateFlow()

    fun getRecordById(recordId: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                _getRecordState.update { ViewModelState.Loading(true) }
                notesRepositoryImpl.getNoteById(recordId).collectLatest { note: Note ->
                    _getRecordState.update { ViewModelState.Success(note) }
                }
            } catch (e: Exception) {
                _getRecordState.update { ViewModelState.Error(e) }
            }
        }
    }

    private val _deleteRecordsState =
        MutableStateFlow<ViewModelState<Unit>>(ViewModelState.Loading(true))
    val deleteRecordsState: StateFlow<ViewModelState<Unit>>
        get() = _deleteRecordsState.asStateFlow()

    fun deleteRecord(note: Note) {
        viewModelScope.launch(dispatcher) {
            try {
                _deleteRecordsState.update { ViewModelState.Loading(true) }
                notesRepositoryImpl.deleteNote(note)
                _deleteRecordsState.update { ViewModelState.Success(Unit) }
            } catch (e: Exception) {
                _deleteRecordsState.update { ViewModelState.Error(e) }
            }
        }
    }
}
