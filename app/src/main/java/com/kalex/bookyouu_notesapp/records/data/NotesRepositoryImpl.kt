package com.kalex.bookyouu_notesapp.records.data

import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.db.data.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val notesDao: NoteDao) : NotesRepository {
    override suspend fun getNotesByDate(subjectID: Int): Flow<List<Note>> {
        return notesDao.getNotesOrderByDate(subjectID)
    }

    override suspend fun getNotesUriOrderByDate(subjectID: Int): Flow<List<String>> {
        return notesDao.getNotesUriOrderByDate(subjectID)
    }

    override suspend fun getPagingNotesByDate(subjectID: Int, limit: Int, offset: Int): List<Note> {
        return notesDao.getPagingNotesOrderByDate(subjectID, limit, offset)
    }

    override suspend fun createNotes(note: Note) {
        notesDao.upsertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }

    override suspend fun getNoteById(recordId: Int): Flow<Note> {
        return notesDao.getNoteById(recordId)
    }

    override suspend fun deleteNotesById(recordId: Int) {
        return notesDao.deleteNoteSById(recordId)
    }
}
