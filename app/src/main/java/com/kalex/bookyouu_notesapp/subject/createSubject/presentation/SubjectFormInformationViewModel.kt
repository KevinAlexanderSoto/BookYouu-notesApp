package com.kalex.bookyouu_notesapp.subject.createSubject.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.subject.createSubject.DayOfWeekStringFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class SubjectFormInformationViewModel @Inject constructor() : ViewModel() {
    private val mapOfDays = mutableStateMapOf<DayOfWeek, Boolean>()
    private val _subjectName = mutableStateOf("")
    private val _classRoom = mutableStateOf("")
    private val _credits = mutableStateOf(0)
    fun isAllFieldsValid() =
        (mapOfDays.isNotEmpty() && mapOfDays.values.contains(true) && _subjectName.value.isNotEmpty() && _classRoom.value.isNotEmpty() && _credits.value != 0)

    fun createSubjectObject() =
        Subject(
            subjectName = _subjectName.value,
            classroom = _classRoom.value,
            credits = _credits.value,
            subjectDay = getListOfSelectedDays(),
        )

    fun addDayOfWeek(day: DayOfWeek) {
        mapOfDays[day] = true
    }

    fun deleteDayOfWeek(day: DayOfWeek) {
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

    private fun getListOfSelectedDays() =
        mapOfDays.filter { map -> map.value }.keys.toMutableList()

    fun getListOfStringSelectedDays() =
        mapOfDays.filter { map -> map.value }.keys.map {
            DayOfWeekStringFactory.getDayStringResource(
                it,
            )
        }
}
