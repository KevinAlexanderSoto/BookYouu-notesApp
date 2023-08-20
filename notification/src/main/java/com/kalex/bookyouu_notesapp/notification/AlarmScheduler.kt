package com.kalex.bookyouu_notesapp.notification

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}
