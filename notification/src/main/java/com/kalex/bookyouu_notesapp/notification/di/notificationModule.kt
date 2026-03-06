package com.kalex.bookyouu_notesapp.notification.di

import android.app.AlarmManager
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.AlarmSchedulerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val notificationModule = module {
    single { androidContext().getSystemService(AlarmManager::class.java) }
    singleOf(::AlarmSchedulerImpl) { bind<AlarmScheduler>() }
}
