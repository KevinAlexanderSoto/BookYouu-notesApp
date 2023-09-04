package com.kalex.bookyouu_notesapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AlarmSchedulerImpl(
    private val context: Context,
    private val alarmManager: AlarmManager,
) : AlarmScheduler {
    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                context,
                item.alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
        context.getSharedPreferences(
            NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_FLAG,
            Context.MODE_PRIVATE,
        )
            .edit()
            .putString(
                NotificationConstants.SHARED_PREFERENCES_NOTIFICATION_STRING,
                "true",
            )
            .apply()
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.alarmId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }
}
