package com.kalex.bookyouu_notesapp.di

import android.content.Context
import androidx.room.Room
import com.kalex.bookyouu_notesapp.db.BookYouuDataBase
import com.kalex.bookyouu_notesapp.db.DBConstants
import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.db.dao.SubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {
    @Provides
    fun provideBookYouuDataBase(@ApplicationContext context: Context): BookYouuDataBase {
        return Room.databaseBuilder(context, BookYouuDataBase::class.java, DBConstants.DB_NAME).build()
    }

    @Provides
    fun provideNoteDao(database: BookYouuDataBase): NoteDao {
        return database.noteDao
    }

    @Provides
    fun provideSubjectDao(database: BookYouuDataBase): SubjectDao {
        return database.subjectDao
    }
}
