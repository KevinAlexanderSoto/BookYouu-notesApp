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
import java.time.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmReceiver : BroadcastReceiver(), KoinComponent {
    private val tapResultIntent: Intent by inject()
    private val alarmScheduler: AlarmScheduler by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("EXTRA_TITLE") ?: NotificationConstants.NOTIFICATION_TITLE
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: NotificationConstants.NOTIFICATION_MESSAGE
        val alarmId = intent?.getIntExtra("EXTRA_ALARM_ID", 78) ?: 78
        val frequencyName = intent?.getStringExtra("EXTRA_FREQUENCY") ?: AlarmFrequency.NONE.name
        val frequency = AlarmFrequency.valueOf(frequencyName)

        // tapResultIntent gets executed when user taps the notification
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent =
            getActivity(context, 99, tapResultIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = context.let {
            NotificationCompat.Builder(it, NotificationConstants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.logoapp)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
        }
        val notificationService =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationService.notify(alarmId, notification)

        // Reschedule based on frequency
        val nextTime = when (frequency) {
            AlarmFrequency.DAILY -> LocalDateTime.now().plusDays(1)
            AlarmFrequency.MONTHLY -> LocalDateTime.now().plusMonths(1)
            AlarmFrequency.NONE -> null
        }

        nextTime?.let {
            alarmScheduler.schedule(
                AlarmItem(
                    alarmId = alarmId,
                    time = it,
                    message = message,
                    title = title,
                    frequency = frequency
                )
            )
        }
    }
}
