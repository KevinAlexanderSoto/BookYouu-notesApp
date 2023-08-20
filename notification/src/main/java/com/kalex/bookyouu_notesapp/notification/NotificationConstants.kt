package com.kalex.bookyouu_notesapp.notification

import java.time.LocalDateTime

object NotificationConstants {
    const val NOTIFICATION_TITLE = "Tus apuntes"
    const val NOTIFICATION_MESSAGE = "No olvides sacar tus apuntes!"
    const val CHANNEL_ID = "app_remainder"
    const val SHARED_PREFERENCES_NOTIFICATION_FLAG = "notification_channel"
    const val SHARED_PREFERENCES_NOTIFICATION_STRING = "notification"
    val alarmItem =
        AlarmItem(
            alarmId = 0,
            time = LocalDateTime.now().plusSeconds((10..40000).random().toLong()),
            message = NOTIFICATION_MESSAGE,
            title = NOTIFICATION_TITLE,
        )
}
