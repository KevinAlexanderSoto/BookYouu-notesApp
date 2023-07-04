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

    @Query("SELECT * FROM note WHERE subject_id == :id ORDER BY note_date DESC")
    fun getNotesOrderByDate(id: Int): Flow<List<Note>>
    @Query("SELECT img_url FROM note WHERE subject_id == :id ORDER BY note_date DESC")
    fun getNotesUriOrderByDate(id: Int): Flow<List<String>>

    @Query("SELECT * FROM note WHERE subject_id == :id ORDER BY note_date DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagingNotesOrderByDate(id: Int, limit: Int, offset: Int): List<Note>

    @Query("SELECT * FROM note WHERE note_id == :id ")
    fun getNoteById(id: Int): Flow<Note>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note WHERE subject_id == :id ")
    suspend fun deleteNoteSById(id: Int)
}
