package com.kalex.bookyouu_notesapp.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investments")
data class InvestmentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String, // e.g., "Stocks", "Crypto", "Cash", "Savings"
    @ColumnInfo(name = "initial_amount")
    val initialAmount: Double,
    @ColumnInfo(name = "currency")
    val currency: String = "USD",
    @ColumnInfo(name = "date_created")
    val dateCreated: Long
)
