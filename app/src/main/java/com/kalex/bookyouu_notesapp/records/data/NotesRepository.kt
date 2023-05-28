package com.kalex.bookyouu_notesapp.records.data

import com.kalex.bookyouu_notesapp.db.data.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getNotesByDate(subjectID: String): Flow<List<Note>>
}