package com.kalex.bookyouu_notesapp.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "obligations")
data class ObligationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "day_of_month")
    val dayOfMonth: Int, // 1 - 31
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "frequency", defaultValue = "MONTHLY")
    val frequency: String = "MONTHLY",
    @ColumnInfo(name = "is_paid")
    val isPaid: Boolean = false,
    @ColumnInfo(name = "last_paid_date")
    val lastPaidDate: Date? = null // Timestamp for "Paid Oct 02"
)
