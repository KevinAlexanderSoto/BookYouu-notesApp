package com.kalex.bookyouu_notesapp.records.data

import com.kalex.bookyouu_notesapp.db.data.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getNotesByDate(subjectID: Int): Flow<List<Note>>
    suspend fun getPagingNotesByDate(subjectID: Int, limit: Int, offset: Int): List<Note>
    suspend fun createNotes(note: Note): Unit
    suspend fun deleteNote(note: Note): Unit
    suspend fun getNoteById(recordId: Int): Flow<Note>
}