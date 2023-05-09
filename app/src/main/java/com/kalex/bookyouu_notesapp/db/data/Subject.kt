package com.kalex.bookyouu_notesapp.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subject_id")
    val subjectId: Int = 0,
    @ColumnInfo(name = "subject_name")
    val subjectName: String,
    @ColumnInfo(name = "classroom")
    val classroom: String,
    @ColumnInfo(name = "subject_day")
    val subjectDay: DayOfWeek,
    @ColumnInfo(name = "credits")
    val credits: Int
)