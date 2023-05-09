package com.kalex.bookyouu_notesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.db.dao.SubjectDao
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.db.typeConvertes.DateTypeConverter
import com.kalex.bookyouu_notesapp.db.typeConvertes.DayOfWeekTypeConverter

@Database(
    entities = [Subject::class, Note::class],
    version = 1
)
@TypeConverters(DateTypeConverter::class, DayOfWeekTypeConverter::class)
abstract class BookYouuDataBase : RoomDatabase() {
    abstract val subjectDao: SubjectDao
    abstract val noteDao: NoteDao
}
