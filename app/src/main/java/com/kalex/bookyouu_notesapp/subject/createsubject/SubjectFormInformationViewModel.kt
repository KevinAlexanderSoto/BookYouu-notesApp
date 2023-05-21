package com.kalex.bookyouu_notesapp.subject.createsubject

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SubjectFormInformationViewModel : ViewModel() {
    private val mapOfDays = mutableStateMapOf<DaysOfWeek, Boolean>()
    private val _subjectName = mutableStateOf("")
    private val _classRoom = mutableStateOf("")
    private val _credits = mutableStateOf(0)
    fun isAllFieldsValid() = (mapOfDays.isNotEmpty() && mapOfDays.values.contains(true) && _subjectName.value.isNotEmpty() && _classRoom.value.isNotEmpty() && _credits.value != 0)

    fun addDayOfWeek(day: DaysOfWeek) {
        mapOfDays[day] = true
    }

    fun deleteDayOfWeek(day: DaysOfWeek) {
        mapOfDays[day] = false
    }

    fun setSubjectName(subjectName: String) {
        _subjectName.value = subjectName
    }

    fun setClassRoomName(classRoomName: String) {
        _classRoom.value = classRoomName
    }

    fun setCredits(credits: Int) {
        _credits.value = credits
    }

    fun getListOfStringSelectedDays() =
        mapOfDays.filter { map -> map.value }.keys.map {
            DayOfWeekStringFactory.getDayStringResource(
                it,
            )
        }
}
