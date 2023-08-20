package com.kalex.bookyouu_notesapp.notification

import java.time.LocalDateTime

data class AlarmItem(
    val alarmId: Int,
    val time: LocalDateTime,
    val message: String,
    val title: String
)