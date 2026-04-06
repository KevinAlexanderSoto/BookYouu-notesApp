package com.kalex.bookyouu_notesapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kalex.bookyouu_notesapp.core.common.getNotificationFlag
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {
    private val alarmScheduler: AlarmScheduler by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val notificationFlag = context.getNotificationFlag()
            if (!notificationFlag.isNullOrEmpty()) {
                // If notification is active, reschedule the default alarm
                alarmScheduler.schedule(NotificationConstants.alarmItem)
            }
        }
    }
}
