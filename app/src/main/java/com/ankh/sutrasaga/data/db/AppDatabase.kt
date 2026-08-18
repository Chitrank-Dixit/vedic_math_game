package com.ankh.sutrasaga.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val worldId: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val bestScore: Int
)

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE worldId = :worldId")
    suspend fun getProgressForWorld(worldId: Int): UserProgressEntity?

    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UserProgressEntity)
}

@Database(
    entities = [
        UserProgressEntity::class,
        UpaSutraProgressEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProgressDao(): UserProgressDao
    abstract fun upaSutraProgressDao(): UpaSutraProgressDao
}
