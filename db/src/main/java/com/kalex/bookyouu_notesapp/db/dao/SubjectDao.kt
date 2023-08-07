package com.kalex.bookyouu_notesapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kalex.bookyouu_notesapp.db.data.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Query("DELETE  FROM subject WHERE subject_id == :subject")
    suspend fun deleteSubject(subject: Int)

    @Query("SELECT * FROM subject")
    fun getSubjects(): Flow<List<Subject>>
}
