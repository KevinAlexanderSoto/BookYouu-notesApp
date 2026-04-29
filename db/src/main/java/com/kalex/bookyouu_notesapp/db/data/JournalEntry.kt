package com.kalex.bookyouu_notesapp.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "journal_entry")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val entryId: Int = 0,
    @ColumnInfo(name = "journal_id")
    val journalId: Int,
    @ColumnInfo(name = "img_url")
    val imgUrl: String,
    @ColumnInfo(name = "voice_uri", defaultValue = "")
    val voiceUri: String,
    @ColumnInfo(name = "entry_date")
    val entryDate: Date,
    @ColumnInfo(name = "entry_description")
    val entryDescription: String
)
