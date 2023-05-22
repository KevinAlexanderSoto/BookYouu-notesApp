package com.kalex.bookyouu_notesapp.subject.subjectList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.subject.createSubject.ViewModelState
import com.kalex.bookyouu_notesapp.subject.domain.SubjectRepository
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
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _getSubjectState =
        MutableStateFlow<ViewModelState<List<Subject>>>(ViewModelState.Loading(true))
    val getSubjectState = _getSubjectState.asStateFlow()

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
}
