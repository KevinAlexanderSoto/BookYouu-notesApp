package com.kalex.bookyouu_notesapp.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "journal")
data class Journal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "journal_id")
    val journalId: Int = 0,
    @ColumnInfo(name = "journal_name")
    val journalName: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "journal_day")
    val journalDay: MutableList<DayOfWeek>,
    @ColumnInfo(name = "priority")
    val priority: Int
)
