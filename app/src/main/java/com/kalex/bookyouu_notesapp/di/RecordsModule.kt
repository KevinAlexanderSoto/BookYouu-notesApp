package com.kalex.bookyouu_notesapp.di

import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import com.kalex.bookyouu_notesapp.records.data.NotesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class RecordsModule {

    @Provides
    fun provideNotesRepository(notesDao: NoteDao): NotesRepository {
        return NotesRepositoryImpl(notesDao)
    }

}
