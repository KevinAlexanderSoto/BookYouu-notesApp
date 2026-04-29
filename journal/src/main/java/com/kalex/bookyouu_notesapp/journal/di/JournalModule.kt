package com.kalex.bookyouu_notesapp.journal.di

import android.media.MediaRecorder
import com.kalex.bookyouu_notesapp.journal.data.JournalEntryRepository
import com.kalex.bookyouu_notesapp.journal.data.JournalEntryRepositoryImpl
import com.kalex.bookyouu_notesapp.journal.data.JournalRepository
import com.kalex.bookyouu_notesapp.journal.data.JournalRepositoryImpl
import com.kalex.bookyouu_notesapp.journal.journalList.presentation.JournalListViewModel
import com.kalex.bookyouu_notesapp.journal.createJournal.presentation.JournalFormViewModel
import com.kalex.bookyouu_notesapp.journal.createJournal.presentation.JournalFormInformationViewModel
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.JournalEntryViewModel
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.PagingJournalEntryViewModel
import com.kalex.bookyouu_notesapp.journal.journalEntry.presentation.AudioJournalEntryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val journalModule = module {
    singleOf(::JournalRepositoryImpl) { bind<JournalRepository>() }
    singleOf(::JournalEntryRepositoryImpl) { bind<JournalEntryRepository>() }

    viewModelOf(::JournalListViewModel)
    viewModelOf(::JournalFormViewModel)
    viewModelOf(::JournalFormInformationViewModel)
    viewModelOf(::JournalEntryViewModel)
    viewModelOf(::PagingJournalEntryViewModel)
    viewModelOf(::AudioJournalEntryViewModel)
}
