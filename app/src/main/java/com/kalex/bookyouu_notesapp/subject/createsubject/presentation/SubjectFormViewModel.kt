package com.kalex.bookyouu_notesapp.subject.createsubject.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.subject.domain.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectFormViewModel @Inject constructor(
    private val repository: SubjectRepository,
) : ViewModel() {

    fun createSubject(subject: Subject) {
        viewModelScope.launch {
            repository.upsertSubjectList(subject)
        }
    }
}
