package com.kalex.bookyouu_notesapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import com.kalex.bookyouu_notesapp.notification.NotificationConstants

@HiltAndroidApp
class BookYouuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
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
