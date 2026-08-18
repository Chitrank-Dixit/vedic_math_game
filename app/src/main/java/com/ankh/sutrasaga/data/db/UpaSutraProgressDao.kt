package com.ankh.sutrasaga.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UpaSutraProgressDao {

    @Query("SELECT * FROM upa_sutra_progress WHERE upaSutraId = :upaSutraId")
    suspend fun getProgressForUpaSutra(upaSutraId: String): UpaSutraProgressEntity?

    @Query("SELECT * FROM upa_sutra_progress")
    fun getAllUpaSutraProgress(): Flow<List<UpaSutraProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UpaSutraProgressEntity)
}
