package com.kalex.bookyouu_notesapp.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.kalex.bookyouu_notesapp.db.dao.ExpenseDao
import com.kalex.bookyouu_notesapp.db.dao.InvestmentDao
import com.kalex.bookyouu_notesapp.db.dao.InvestmentTransactionDao
import com.kalex.bookyouu_notesapp.db.dao.JournalEntryDao
import com.kalex.bookyouu_notesapp.db.dao.ObligationDao
import com.kalex.bookyouu_notesapp.db.dao.JournalDao
import com.kalex.bookyouu_notesapp.db.data.ExpenseEntity
import com.kalex.bookyouu_notesapp.db.data.InvestmentEntity
import com.kalex.bookyouu_notesapp.db.data.InvestmentTransactionEntity
import com.kalex.bookyouu_notesapp.db.data.JournalEntry
import com.kalex.bookyouu_notesapp.db.data.ObligationEntity
import com.kalex.bookyouu_notesapp.db.data.Journal
import com.kalex.bookyouu_notesapp.db.typeConvertes.DateTypeConverter
import com.kalex.bookyouu_notesapp.db.typeConvertes.DayOfWeekTypeConverter

@Database(
    entities = [
        Journal::class,
        JournalEntry::class,
        ObligationEntity::class,
        ExpenseEntity::class,
        InvestmentEntity::class,
        InvestmentTransactionEntity::class
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = BookYouuDataBase.Migration2To3::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5)
    ]
)
@TypeConverters(DateTypeConverter::class, DayOfWeekTypeConverter::class)
abstract class BookYouuDataBase : RoomDatabase() {
    abstract val journalDao: JournalDao
    abstract val journalEntryDao: JournalEntryDao
    abstract val obligationDao: ObligationDao
    abstract val expenseDao: ExpenseDao
    abstract val investmentDao: InvestmentDao
    abstract val investmentTransactionDao: InvestmentTransactionDao

    @RenameTable(fromTableName = "Subject", toTableName = "journal")
    @RenameColumn(tableName = "Subject", fromColumnName = "subject_id", toColumnName = "journal_id")
    @RenameColumn(tableName = "Subject", fromColumnName = "subject_name", toColumnName = "journal_name")
    @RenameColumn(tableName = "Subject", fromColumnName = "classroom", toColumnName = "location")
    @RenameColumn(tableName = "Subject", fromColumnName = "subject_day", toColumnName = "journal_day")
    @RenameColumn(tableName = "Subject", fromColumnName = "credits", toColumnName = "priority")
    @RenameTable(fromTableName = "Note", toTableName = "journal_entry")
    @RenameColumn(tableName = "Note", fromColumnName = "note_id", toColumnName = "entry_id")
    @RenameColumn(tableName = "Note", fromColumnName = "note_date", toColumnName = "entry_date")
    @RenameColumn(tableName = "Note", fromColumnName = "note_description", toColumnName = "entry_description")
    @RenameColumn(tableName = "Note", fromColumnName = "subject_id", toColumnName = "journal_id")
    class Migration2To3 : AutoMigrationSpec
}
