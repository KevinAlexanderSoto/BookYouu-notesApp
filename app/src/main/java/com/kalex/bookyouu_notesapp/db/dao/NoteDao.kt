package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kalex.bookyouu_notesapp.db.data.Note
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Query("SELECT * FROM note WHERE subject_id == :id ORDER BY note_date")
    fun getNotesOrderByDate(id: Int): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)
}
