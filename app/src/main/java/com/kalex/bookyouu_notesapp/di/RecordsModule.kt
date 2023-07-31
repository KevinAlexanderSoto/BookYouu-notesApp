package com.kalex.bookyouu_notesapp.di

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import com.kalex.bookyouu_notesapp.records.data.NotesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RecordsModule {

    @Provides
    fun provideNotesRepository(notesDao: NoteDao): NotesRepository {
        return NotesRepositoryImpl(notesDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class AnalyticsModule {
    @Provides
    fun provideMediaRecorder(@ApplicationContext context: Context): MediaRecorder {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            MediaRecorder()
        } else {
            MediaRecorder(context)
        }
    }
}
