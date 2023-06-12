package com.kalex.bookyouu_notesapp.subject.createSubject.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.common.ViewModelState
import com.kalex.bookyouu_notesapp.subject.data.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectFormViewModel @Inject constructor(
    private val repository: SubjectRepository,
) : ViewModel() {
    private val _createSubjectState = MutableStateFlow<ViewModelState<Boolean>>(ViewModelState.Loading(true))
    val createSubjectState = _createSubjectState.asStateFlow()
    fun createSubject(subject: Subject) {
        viewModelScope.launch {
            try {
                _createSubjectState.update { ViewModelState.Loading(true) }
                repository.upsertSubjectList(subject)
                _createSubjectState.update { ViewModelState.Success(true) }
            } catch (e: Exception) {
                _createSubjectState.update { ViewModelState.Error(e) } }
        }
    }
}
