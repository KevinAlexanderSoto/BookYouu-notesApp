package com.kalex.bookyouu_notesapp.subject.createSubject

import com.kalex.bookyouu_notesapp.R
import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY

object DayOfWeekStringFactory {
    fun getDayStringResource(day: DayOfWeek) =
        when (day) {
            MONDAY -> R.string.subject_form_monday
            TUESDAY -> R.string.subject_form_tuesday
            WEDNESDAY -> R.string.subject_form_wednesday
            THURSDAY -> R.string.subject_form_thursday
            FRIDAY -> R.string.subject_form_friday
            SATURDAY -> R.string.subject_form_saturday
            SUNDAY -> R.string.subject_form_sunday
        }
}
