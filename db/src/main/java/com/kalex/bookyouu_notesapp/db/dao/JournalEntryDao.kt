package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kalex.bookyouu_notesapp.db.data.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {

    @Upsert
    suspend fun upsertJournalEntry(entry: JournalEntry)

    @Query("SELECT * FROM journal_entry WHERE journal_id == :id ORDER BY entry_date DESC")
    fun getJournalEntriesOrderByDate(id: Int): Flow<List<JournalEntry>>

    @Query("SELECT img_url FROM journal_entry WHERE journal_id == :id ORDER BY entry_date DESC")
    fun getJournalEntriesUriOrderByDate(id: Int): Flow<List<String>>

    @Query("SELECT * FROM journal_entry WHERE journal_id == :id ORDER BY entry_date DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagingJournalEntriesOrderByDate(id: Int, limit: Int, offset: Int): List<JournalEntry>

    @Query("SELECT * FROM journal_entry WHERE entry_id == :id ")
    fun getJournalEntryById(id: Int): Flow<JournalEntry>

    @Delete
    suspend fun deleteJournalEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entry WHERE journal_id == :id ")
    suspend fun deleteJournalEntriesByJournalId(id: Int)
}
