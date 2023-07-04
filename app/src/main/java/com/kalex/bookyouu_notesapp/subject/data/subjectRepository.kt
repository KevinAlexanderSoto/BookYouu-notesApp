package com.kalex.bookyouu_notesapp.subject.data

import com.kalex.bookyouu_notesapp.db.data.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    fun getSubjectList(): Flow<List<Subject>>
    suspend fun upsertSubjectList(subject: Subject)
    suspend fun deleteSubject(subject: Int)
}