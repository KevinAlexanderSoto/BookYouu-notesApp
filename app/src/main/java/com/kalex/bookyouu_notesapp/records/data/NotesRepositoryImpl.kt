package com.kalex.bookyouu_notesapp.records.data

import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.db.data.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val notesDao: NoteDao) : NotesRepository {
    override suspend fun getNotesByDate(subjectID: String): Flow<List<Note>> {
        return notesDao.getNotesOrderByDate(subjectID.toInt())
    }
}
