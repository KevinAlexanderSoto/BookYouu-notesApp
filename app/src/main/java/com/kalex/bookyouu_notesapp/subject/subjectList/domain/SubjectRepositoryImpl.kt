package com.kalex.bookyouu_notesapp.subject.subjectList.domain

import com.kalex.bookyouu_notesapp.db.dao.SubjectDao
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
) : SubjectRepository {
    override fun getSubjectList() = subjectDao.getSubjects()
}
