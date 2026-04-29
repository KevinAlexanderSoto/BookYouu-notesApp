package com.kalex.bookyouu_notesapp.journal.data

import com.kalex.bookyouu_notesapp.db.dao.JournalEntryDao
import com.kalex.bookyouu_notesapp.db.data.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalEntryRepositoryImpl(private val journalEntryDao: JournalEntryDao) : JournalEntryRepository {
    override suspend fun getJournalEntriesByDate(journalId: Int): Flow<List<JournalEntry>> {
        return journalEntryDao.getJournalEntriesOrderByDate(journalId)
    }

    override suspend fun getJournalEntriesUriOrderByDate(journalId: Int): Flow<List<String>> {
        return journalEntryDao.getJournalEntriesUriOrderByDate(journalId)
    }

    override suspend fun getPagingJournalEntriesByDate(journalId: Int, limit: Int, offset: Int): List<JournalEntry> {
        return journalEntryDao.getPagingJournalEntriesOrderByDate(journalId, limit, offset)
    }

    override suspend fun createJournalEntry(entry: JournalEntry) {
        journalEntryDao.upsertJournalEntry(entry)
    }

    override suspend fun deleteJournalEntry(entry: JournalEntry) {
        journalEntryDao.deleteJournalEntry(entry)
    }

    override suspend fun getJournalEntryById(entryId: Int): Flow<JournalEntry> {
        return journalEntryDao.getJournalEntryById(entryId)
    }

    override suspend fun deleteJournalEntriesByJournalId(journalId: Int) {
        return journalEntryDao.deleteJournalEntriesByJournalId(journalId)
    }
}
