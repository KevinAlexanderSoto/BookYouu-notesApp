package com.kalex.bookyouu_notesapp.di

import android.app.Application
import android.content.Context
import android.content.Intent
import com.kalex.bookyouu_notesapp.MainActivity
import com.kalex.bookyouu_notesapp.authentication.BiometricSupportUseCase
import com.kalex.bookyouu_notesapp.moreMenu.MoreMenuFlagsUseCase
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
    @Provides
    fun provideBiometricSupportUseCase(@ApplicationContext context: Context): BiometricSupportUseCase {
        return BiometricSupportUseCase(context)
    }
    @Provides
    fun provideMoreMenuFlagsUseCase(@ApplicationContext context: Context): MoreMenuFlagsUseCase {
        return MoreMenuFlagsUseCase(context)
    }
}
