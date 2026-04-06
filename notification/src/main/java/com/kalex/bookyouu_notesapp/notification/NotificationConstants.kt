package com.kalex.bookyouu_notesapp.notification

import java.time.LocalDateTime

object NotificationConstants {
    const val NOTIFICATION_TITLE = "Tus apuntes"
    const val NOTIFICATION_MESSAGE = "No olvides sacar tus apuntes!"
    const val CHANNEL_ID = "app_remainder"
    const val SHARED_PREFERENCES_NOTIFICATION_FLAG = "notification_channel"
    const val SHARED_PREFERENCES_NOTIFICATION_STRING = "notification"
    const val DEFAULT_ALARM_ID = 1001

    val alarmItem: AlarmItem
        get() {
            var alarmTime = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0)
            if (alarmTime.isBefore(LocalDateTime.now())) {
                alarmTime = alarmTime.plusDays(1)
            }
            return AlarmItem(
                alarmId = DEFAULT_ALARM_ID,
                time = alarmTime,
                message = NOTIFICATION_MESSAGE,
                title = NOTIFICATION_TITLE,
            )
        }
}
