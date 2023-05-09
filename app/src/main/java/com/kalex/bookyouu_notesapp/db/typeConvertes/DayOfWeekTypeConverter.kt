package com.kalex.bookyouu_notesapp.db.typeConvertes

import androidx.room.TypeConverter
import java.time.DayOfWeek

class DayOfWeekTypeConverter {
    @TypeConverter
    fun daysOfWeekToString(daysOfWeek: MutableList<DayOfWeek>?): String? =
        daysOfWeek?.map { it.value }?.joinToString(separator = SEPARATOR)

    @TypeConverter
    fun stringToDaysOfWeek(daysOfWeek: String?): MutableList<DayOfWeek>? =
        daysOfWeek?.split(SEPARATOR)?.map { DayOfWeek.of(it.toInt()) }?.toMutableList()

    companion object {
        private const val SEPARATOR = "/"
    }
}