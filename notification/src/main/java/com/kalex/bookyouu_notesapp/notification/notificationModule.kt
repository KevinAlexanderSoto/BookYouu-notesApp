package com.kalex.bookyouu_notesapp.notification

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class NotificationModule {
    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(AlarmManager::class.java)
    }

    @Provides
    fun provideAlarmScheduler(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager,
    ): AlarmScheduler {
        return AlarmSchedulerImpl(context, alarmManager)
    }
}
