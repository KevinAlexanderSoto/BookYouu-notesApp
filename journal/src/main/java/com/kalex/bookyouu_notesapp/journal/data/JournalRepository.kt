package com.kalex.bookyouu_notesapp.journal.data

import com.kalex.bookyouu_notesapp.db.data.Journal
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getJournalList(): Flow<List<Journal>>
    suspend fun upsertJournal(journal: Journal)
    suspend fun deleteJournal(journalId: Int)
}
