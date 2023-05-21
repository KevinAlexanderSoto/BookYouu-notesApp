package com.kalex.bookyouu_notesapp.subject.createsubject

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class SubjectFormInformationViewModel : ViewModel() {
    private val mapOfDays = mutableStateMapOf<DaysOfWeek, Boolean>()

    fun addDayOfWeek(day: DaysOfWeek) {
        mapOfDays[day] = true
    }
    fun deleteDayOfWeek(day: DaysOfWeek) {
        mapOfDays[day] = false
    }

    fun getListOfStringSelectedDays() =
        mapOfDays.filter { map -> map.value }.keys.map { DayOfWeekStringFactory.getDayStringResource(it) }

}
