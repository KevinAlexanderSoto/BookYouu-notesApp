package com.kalex.bookyouu_notesapp.records

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_MEDIA_IMAGES,
            )
        }
        else {
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    val permissionsList: List<String>
        get() = REQUIRED_PERMISSIONS

    private val _getRecordsState =
        MutableStateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<List<Note>>>(com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true))
    val getRecordsState: StateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<List<Note>>>
        get() = _getRecordsState.asStateFlow()

    fun getRecordsList(subjectId: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                _getRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true) }
                notesRepositoryImpl.getNotesByDate(subjectId).collectLatest { list ->
                    if (list.isEmpty()) {
                        _getRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Empty() }
                    } else {
                        _getRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Success(list) }
                    }
                }
            } catch (e: Exception) {
                _getRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Error(e) }
            }
        }
    }

    private val _saveRecordsState =
        MutableStateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<Unit>>(com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true))
    val saveRecordsState: StateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<Unit>>
        get() = _saveRecordsState.asStateFlow()

    fun createRecord(
        subjectId: Int,
        imgUrl: String,
        voiceNoteUri: String,
        noteDescription: String,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                _saveRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true) }
                notesRepositoryImpl.createNotes(
                    Note(
                        subjectId = subjectId,
                        imgUrl = imgUrl,
                        noteDate = Date(),
                        voiceUri = voiceNoteUri,
                        noteDescription = noteDescription,
                    ),
                )
                _saveRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Success(Unit) }
            } catch (e: Exception) {
                _saveRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Error(e) }
            }
        }
    }

    private val _getRecordState =
        MutableStateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<Note>>(com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true))
    val getRecordState: StateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<Note>>
        get() = _getRecordState.asStateFlow()

    fun getRecordById(recordId: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                _getRecordState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true) }
                notesRepositoryImpl.getNoteById(recordId).collectLatest { note: Note ->
                    _getRecordState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Success(note) }
                }
            } catch (e: Exception) {
                _getRecordState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Error(e) }
            }
        }
    }

    private val _deleteRecordsState =
        MutableStateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<Unit>>(com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true))
    val deleteRecordsState: StateFlow<com.kalex.bookyouu_notesapp.core.common.ViewModelState<Unit>>
        get() = _deleteRecordsState.asStateFlow()

    fun deleteRecord(note: Note) {
        viewModelScope.launch(dispatcher) {
            try {
                _deleteRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Loading(true) }
                notesRepositoryImpl.deleteNote(note)
                _deleteRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Success(Unit) }
            } catch (e: Exception) {
                _deleteRecordsState.update { com.kalex.bookyouu_notesapp.core.common.ViewModelState.Error(e) }
            }
        }
    }
}
