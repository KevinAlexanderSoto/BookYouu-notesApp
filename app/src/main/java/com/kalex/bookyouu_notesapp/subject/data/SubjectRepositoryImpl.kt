package com.kalex.bookyouu_notesapp.subject.data

import com.kalex.bookyouu_notesapp.db.dao.SubjectDao
import com.kalex.bookyouu_notesapp.db.data.Subject
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
) : SubjectRepository {
    override fun getSubjectList() = subjectDao.getSubjects()
    override suspend fun upsertSubjectList(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override suspend fun deleteSubject(subject: Int) {
        subjectDao.deleteSubject(subject)
    }
}
