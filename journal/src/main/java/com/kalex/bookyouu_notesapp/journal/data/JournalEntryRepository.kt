package com.kalex.bookyouu_notesapp.journal.data

import com.kalex.bookyouu_notesapp.db.data.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalEntryRepository {
    suspend fun getJournalEntriesByDate(journalId: Int): Flow<List<JournalEntry>>
    suspend fun getJournalEntriesUriOrderByDate(journalId: Int): Flow<List<String>>
    suspend fun getPagingJournalEntriesByDate(journalId: Int, limit: Int, offset: Int): List<JournalEntry>
    suspend fun createJournalEntry(entry: JournalEntry): Unit
    suspend fun deleteJournalEntry(entry: JournalEntry): Unit
    suspend fun deleteJournalEntriesByJournalId(journalId: Int)
    suspend fun getJournalEntryById(entryId: Int): Flow<JournalEntry>
}
