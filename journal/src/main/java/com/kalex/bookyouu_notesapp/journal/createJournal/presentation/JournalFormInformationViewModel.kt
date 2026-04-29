package com.kalex.bookyouu_notesapp.journal.createJournal.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kalex.bookyouu_notesapp.db.data.Journal
import com.kalex.bookyouu_notesapp.journal.createJournal.DayOfWeekStringFactory
import java.time.DayOfWeek

class JournalFormInformationViewModel : ViewModel() {
    private val mapOfDays = mutableStateMapOf<DayOfWeek, Boolean>()
    private val _journalName = mutableStateOf("")
    private val _location = mutableStateOf("")
    private val _priority = mutableStateOf(0)
    
    fun isAllFieldsValid() =
        (mapOfDays.isNotEmpty() && mapOfDays.values.contains(true) && _journalName.value.isNotEmpty())

    fun createJournalObject() =
        Journal(
            journalName = _journalName.value,
            location = _location.value,
            priority = _priority.value,
            journalDay = getListOfSelectedDays(),
        )

    fun addDayOfWeek(day: DayOfWeek) {
        mapOfDays[day] = true
    }

    fun deleteDayOfWeek(day: DayOfWeek) {
        mapOfDays[day] = false
    }

    fun setJournalName(name: String) {
        _journalName.value = name
    }

    fun setLocation(location: String) {
        _location.value = location
    }

    fun setPriority(priority: Int) {
        _priority.value = priority
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
