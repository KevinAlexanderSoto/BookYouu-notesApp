package com.kalex.bookyouu_notesapp.di

import com.kalex.bookyouu_notesapp.db.dao.SubjectDao
import com.kalex.bookyouu_notesapp.subject.subjectList.domain.SubjectRepository
import com.kalex.bookyouu_notesapp.subject.subjectList.domain.SubjectRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class SubjectModule {
    @Provides
    fun provideRepository(subjectDao: SubjectDao): SubjectRepository {
        return SubjectRepositoryImpl(subjectDao)
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
