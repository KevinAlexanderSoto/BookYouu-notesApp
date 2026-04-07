package com.kalex.bookyouu_notesapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.kalex.bookyouu_notesapp.authentication.di.authenticationModule
import com.kalex.bookyouu_notesapp.di.appModule
import com.kalex.bookyouu_notesapp.di.databaseModule
import com.kalex.bookyouu_notesapp.di.repositoryModule
import com.kalex.bookyouu_notesapp.di.viewModelModule
import com.kalex.bookyouu_notesapp.expenses.di.expensesModule
import com.kalex.bookyouu_notesapp.notification.di.notificationModule
import com.kalex.bookyouu_notesapp.payments.di.paymentsModule
import com.kalex.bookyouu_notesapp.notification.NotificationConstants
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookYouuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookYouuApplication)
            modules(
                appModule,
                databaseModule,
                repositoryModule,
                viewModelModule,
                authenticationModule,
                notificationModule,
                paymentsModule,
                expensesModule
            )
        }
        configureNotificationChannel(this)
    }
}

fun configureNotificationChannel(bookYouuApplication: BookYouuApplication) {
    val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, NotificationConstants.CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT).apply {
        description = "Notification to take notes"
    }

    // Register the channel with the system
    val notificationManager = bookYouuApplication.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.createNotificationChannel(channel)
}
