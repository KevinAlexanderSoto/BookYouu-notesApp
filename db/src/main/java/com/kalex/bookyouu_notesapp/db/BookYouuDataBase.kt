package com.kalex.bookyouu_notesapp.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kalex.bookyouu_notesapp.db.dao.ExpenseDao
import com.kalex.bookyouu_notesapp.db.dao.NoteDao
import com.kalex.bookyouu_notesapp.db.dao.ObligationDao
import com.kalex.bookyouu_notesapp.db.dao.SubjectDao
import com.kalex.bookyouu_notesapp.db.data.ExpenseEntity
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.db.data.ObligationEntity
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.db.typeConvertes.DateTypeConverter
import com.kalex.bookyouu_notesapp.db.typeConvertes.DayOfWeekTypeConverter

@Database(
    entities = [Subject::class, Note::class, ObligationEntity::class, ExpenseEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(DateTypeConverter::class, DayOfWeekTypeConverter::class)
abstract class BookYouuDataBase : RoomDatabase() {
    abstract val subjectDao: SubjectDao
    abstract val noteDao: NoteDao
    abstract val obligationDao: ObligationDao
    abstract val expenseDao: ExpenseDao
}
