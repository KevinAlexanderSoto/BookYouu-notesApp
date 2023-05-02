package com.kalex.bookyouu_notesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val note_id: Int,
    val subject_id: Int,
    val img_url: String,
    val note_date: Date,
    val note_description: String
)
