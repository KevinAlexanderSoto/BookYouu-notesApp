package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kalex.bookyouu_notesapp.db.data.Journal
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Upsert
    suspend fun upsertJournal(journal: Journal)

    @Query("DELETE FROM journal WHERE journal_id == :journalId")
    suspend fun deleteJournal(journalId: Int)

    @Query("SELECT * FROM journal")
    fun getJournals(): Flow<List<Journal>>
}
