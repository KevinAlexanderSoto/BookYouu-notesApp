package com.kalex.bookyouu_notesapp.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val noteId: Int = 0,
    @ColumnInfo(name = "subject_id")
    val subjectId: Int,
    @ColumnInfo(name = "img_url")
    val imgUrl: String,
    @ColumnInfo(name = "voice_uri", defaultValue = "")
    val voiceUri: String,
    @ColumnInfo(name = "note_date")
    val noteDate: Date,
    @ColumnInfo(name = "note_description")
    val noteDescription: String
)
