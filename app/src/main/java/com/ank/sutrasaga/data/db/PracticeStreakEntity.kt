package com.ank.sutrasaga.data.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Room entity representing the user's persisted practice streak.
 * Stored as a single row with id = 1.
 */
@Entity(tableName = "practice_streak")
data class PracticeStreakEntity(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int = 0,
    val lastPracticeDate: String? = null // ISO-8601 "YYYY-MM-DD"
)

@Dao
interface PracticeStreakDao {
    @Query("SELECT * FROM practice_streak WHERE id = 1")
    fun getStreakFlow(): Flow<PracticeStreakEntity?>

    @Query("SELECT * FROM practice_streak WHERE id = 1")
    suspend fun getStreak(): PracticeStreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStreak(streak: PracticeStreakEntity)
}
