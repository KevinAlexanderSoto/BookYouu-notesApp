package com.kalex.bookyouu_notesapp.subject.createsubject

import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.FRIDAY
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.MONDAY
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.SATURDAY
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.SUNDAY
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.THURSDAY
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.TUESDAY
import com.kalex.bookyouu_notesapp.subject.createsubject.DaysOfWeek.WEDNESDAY

object DayOfWeekStringFactory {
    fun getDayStringResource(day: DaysOfWeek) =
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
