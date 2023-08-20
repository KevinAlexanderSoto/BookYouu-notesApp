package com.kalex.bookyouu_notesapp.di

import android.content.Context
import android.content.Intent
import com.kalex.bookyouu_notesapp.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    fun provideMainActivity(@ApplicationContext context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }
}
