package com.kalex.bookyouu_notesapp.subject.subjectList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import com.kalex.bookyouu_notesapp.subject.data.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectListViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val notesRepositoryImpl: NotesRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _getSubjectState =
        MutableStateFlow<ViewModelState<List<Subject>>>(ViewModelState.Loading(true))
    val getSubjectState = _getSubjectState.asStateFlow()

    private val _getNotesUrlState =
        MutableStateFlow<ViewModelState<List<String>>>(ViewModelState.Loading(false))
    val getNotesUrlState = _getNotesUrlState.asStateFlow()

    fun getSubjectList() {
        viewModelScope.launch(dispatcher) {
            try {
                subjectRepository.getSubjectList().collectLatest { subjects ->
                    if (subjects.isEmpty()) {
                        _getSubjectState.update {
                            ViewModelState.Empty()
                        }
                    } else {
                        _getSubjectState.update {
                            ViewModelState.Success(subjects)
                        }
                    }
                }
            } catch (e: Exception) {
                _getSubjectState.update {
                    ViewModelState.Error(e)
                }
            }
        }
    }

    fun getNotesUrl(subjectId: Int) {
        viewModelScope.launch(dispatcher) {
            notesRepositoryImpl.getNotesUriOrderByDate(subjectId).collectLatest { listNoteUrl ->
                try {
                    if (listNoteUrl.isEmpty()) {
                        _getNotesUrlState.update {
                            ViewModelState.Empty()
                        }
                    } else {
                        _getNotesUrlState.update {
                            ViewModelState.Success(listNoteUrl)
                        }
                    }
                } catch (e: Exception) {
                    _getSubjectState.update {
                        ViewModelState.Error(e)
                    }
                }
            }
        }
    }

    fun deleteSubject(subjectId: Int) {
        viewModelScope.launch(dispatcher) {
            notesRepositoryImpl.deleteNotesById(subjectId)
            subjectRepository.deleteSubject(subjectId)
        }
    }
}
