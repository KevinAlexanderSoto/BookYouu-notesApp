package com.kalex.bookyouu_notesapp.subject.subjectList

import com.kalex.bookyouu_notesapp.db.data.Subject

data class GetSubjectState(
    var isLoading: Boolean = false,
    var response: List<Subject>? = null,
    var error: String = "",
)
