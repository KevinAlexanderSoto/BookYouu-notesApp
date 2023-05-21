package com.kalex.bookyouu_notesapp.subject.subjectList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.subject.subjectList.GetSubjectState
import com.kalex.bookyouu_notesapp.subject.subjectList.domain.SubjectRepository
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
    private val _getSubjectState = MutableStateFlow(GetSubjectState())
    val getSubjectState = _getSubjectState.asStateFlow()

    fun getSubjectList() {
        _getSubjectState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(dispatcher) {
            subjectRepository.getSubjectList().collectLatest { subjects ->
                _getSubjectState.update {
                    it.copy(
                        isLoading = false,
                        response = subjects.ifEmpty { null },
                    )
                }
            }
        }
    }
}
