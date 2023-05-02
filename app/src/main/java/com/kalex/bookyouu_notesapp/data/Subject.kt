package com.kalex.bookyouu_notesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val subject_id: Int = 0,
    val subject_name: String,
    val classroom: String,
    val subject_day: DayOfWeek,
    val credits: Int
)