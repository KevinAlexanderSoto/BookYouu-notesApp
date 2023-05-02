package com.kalex.bookyouu_notesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kalex.bookyouu_notesapp.data.Note
import com.kalex.bookyouu_notesapp.data.Subject
import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.db.dao.SubjectDao

@Database(
    entities = [Subject::class, Note::class],
    version = 1
)
abstract class BookYouuDataBase : RoomDatabase() {
    abstract val subjectDao: SubjectDao
    abstract val noteDao: NoteDao
}
