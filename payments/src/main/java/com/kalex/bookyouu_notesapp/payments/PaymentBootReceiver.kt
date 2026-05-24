package com.kalex.bookyouu_notesapp.payments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kalex.bookyouu_notesapp.notification.AlarmFrequency
import com.kalex.bookyouu_notesapp.notification.AlarmItem
import com.kalex.bookyouu_notesapp.notification.AlarmScheduler
import com.kalex.bookyouu_notesapp.notification.AlarmUtils
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetObligationsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class PaymentBootReceiver: BroadcastReceiver(), KoinComponent {
    private val getObligationsUseCase: GetObligationsUseCase by inject()
    private val alarmScheduler: AlarmScheduler by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        // Reschedule obligations
        scope.launch {
            try {
                val summary = getObligationsUseCase().first()
                summary.obligations.filter { it.reminderEnabled }.forEach { obligation ->
                    val alarmTime = AlarmUtils.calculateNextMonthlyAlarmTime(obligation.dayOfMonth)
                    alarmScheduler.schedule(
                        AlarmItem(
                            alarmId = obligation.id,
                            time = alarmTime,
                            message = context?.getString(
                                R.string.notification_obligation_message,
                                obligation.name
                            ) ?: "Don\\'t forget to pay: ${obligation.name}",
                            title = context?.getString(R.string.notification_obligation_title) ?: "Payment Reminder",
                            frequency = AlarmFrequency.MONTHLY
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}