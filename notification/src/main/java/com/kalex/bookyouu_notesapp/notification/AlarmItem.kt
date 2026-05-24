package com.kalex.bookyouu_notesapp.notification

import java.time.LocalDateTime

enum class AlarmFrequency {
    DAILY,
    MONTHLY,
    NONE
}

data class AlarmItem(
    val alarmId: Int,
    val time: LocalDateTime,
    val message: String,
    val title: String,
    val frequency: AlarmFrequency = AlarmFrequency.NONE
)