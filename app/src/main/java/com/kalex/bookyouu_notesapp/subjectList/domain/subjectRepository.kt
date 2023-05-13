package com.kalex.bookyouu_notesapp.subjectList.domain

import com.kalex.bookyouu_notesapp.db.data.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    fun getSubjectList(): Flow<List<Subject>>
}