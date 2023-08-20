package com.kalex.bookyouu_notesapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import javax.inject.Inject

class AlarmReceiver() : BroadcastReceiver() {
    @Inject
    lateinit var tapResultIntent: Intent
    override fun onReceive(context: Context, intent: Intent?) {
        // tapResultIntent gets executed when user taps the notification
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent =
            getActivity(context, 99, tapResultIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = context.let {
            NotificationCompat.Builder(it, NotificationConstants.CHANNEL_ID)
                .setContentTitle(NotificationConstants.NOTIFICATION_TITLE)
                .setContentText(NotificationConstants.NOTIFICATION_MESSAGE)
                .setSmallIcon(R.mipmap.logoapp)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
        }
        val notificationService =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationService.notify(78, notification)
    }
}
