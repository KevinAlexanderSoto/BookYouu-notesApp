package com.kalex.bookyouu_notesapp.journal.data

import com.kalex.bookyouu_notesapp.db.dao.JournalDao
import com.kalex.bookyouu_notesapp.db.data.Journal

class JournalRepositoryImpl(
    private val journalDao: JournalDao,
) : JournalRepository {
    override fun getJournalList() = journalDao.getJournals()
    override suspend fun upsertJournal(journal: Journal) {
        journalDao.upsertJournal(journal)
    }

    override suspend fun deleteJournal(journalId: Int) {
        journalDao.deleteJournal(journalId)
    }
}
