package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kalex.bookyouu_notesapp.db.data.ObligationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObligationDao {
    @Query("SELECT * FROM obligations ORDER BY is_paid ASC, day_of_month ASC")
    fun getMonthlyObligations(): Flow<List<ObligationEntity>>

    @Update
    suspend fun updateObligation(obligation: ObligationEntity)
    
    @Insert
    suspend fun insertObligation(obligation: ObligationEntity)

    @Query("DELETE FROM obligations WHERE id = :id")
    suspend fun deleteObligation(id: Int)

    @Query("DELETE FROM obligations WHERE id IN (:ids)")
    suspend fun deleteObligations(ids: List<Int>)
}
