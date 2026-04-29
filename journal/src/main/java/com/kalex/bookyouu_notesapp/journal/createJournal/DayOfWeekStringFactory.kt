package com.kalex.bookyouu_notesapp.journal.createJournal

import com.kalex.bookyouu_notesapp.journal.R
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
            MONDAY -> R.string.journal_form_monday
            TUESDAY -> R.string.journal_form_tuesday
            WEDNESDAY -> R.string.journal_form_wednesday
            THURSDAY -> R.string.journal_form_thursday
            FRIDAY -> R.string.journal_form_friday
            SATURDAY -> R.string.journal_form_saturday
            SUNDAY -> R.string.journal_form_sunday
        }
}
