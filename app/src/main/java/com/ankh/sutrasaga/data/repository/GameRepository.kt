package com.ankh.sutrasaga.data.repository

import android.util.Log
import com.ankh.sutrasaga.data.db.UpaSutraProgressDao
import com.ankh.sutrasaga.data.db.UpaSutraProgressEntity
import com.ankh.sutrasaga.data.db.UserProgressDao
import com.ankh.sutrasaga.data.db.UserProgressEntity
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(
    private val userDao: UserProgressDao,
    private val upaDao: UpaSutraProgressDao? = null
) {
    companion object {
        private const val TAG = "GameRepository"
    }

    fun getAllProgress(): Flow<List<UserProgressEntity>> = userDao.getAllProgress()

    suspend fun getProgressForWorld(worldId: Int): UserProgressEntity? {
        return try {
            userDao.getProgressForWorld(worldId)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching progress for world $worldId", e)
            null
        }
    }

    suspend fun saveWorldCompletion(worldId: Int, score: Int) {
        try {
            val existing = userDao.getProgressForWorld(worldId)
            val bestScore = if (existing != null) maxOf(existing.bestScore, score) else score
            userDao.saveProgress(
                UserProgressEntity(
                    worldId = worldId,
                    isUnlocked = true,
                    isCompleted = true,
                    bestScore = bestScore
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error saving world completion for world $worldId", e)
        }
    }

    fun getAllUpaSutraProgress(): Flow<List<UpaSutraProgress>> {
        return upaDao?.getAllUpaSutraProgress()?.map { list ->
            list.mapNotNull { entity ->
                try {
                    val id = UpaSutraId.valueOf(entity.upaSutraId)
                    val state = UpaSutraCompletionState.valueOf(entity.state)
                    UpaSutraProgress(
                        id = id,
                        state = state,
                        practiceCorrectCount = entity.practiceCorrectCount,
                        challengeCorrectCount = entity.challengeCorrectCount,
                        lastAttemptTimestamp = entity.lastAttemptTimestamp
                    )
                } catch (e: IllegalArgumentException) {
                    Log.w(TAG, "Unknown UpaSutraId or state in entity: ${entity.upaSutraId}, state=${entity.state}", e)
                    null
                } catch (e: Exception) {
                    Log.e(TAG, "Unexpected error converting entity: $entity", e)
                    null
                }
            }
        } ?: kotlinx.coroutines.flow.flowOf(emptyList())
    }

    suspend fun getUpaSutraProgress(id: UpaSutraId): UpaSutraProgress? {
        val entity = upaDao?.getProgressForUpaSutra(id.name) ?: return null
        return try {
            UpaSutraProgress(
                id = UpaSutraId.valueOf(entity.upaSutraId),
                state = UpaSutraCompletionState.valueOf(entity.state),
                practiceCorrectCount = entity.practiceCorrectCount,
                challengeCorrectCount = entity.challengeCorrectCount,
                lastAttemptTimestamp = entity.lastAttemptTimestamp
            )
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "Invalid UpaSutraProgress record for $id", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching UpaSutraProgress for $id", e)
            null
        }
    }

    suspend fun saveUpaSutraProgress(progress: UpaSutraProgress) {
        try {
            upaDao?.saveProgress(
                UpaSutraProgressEntity(
                    upaSutraId = progress.id.name,
                    state = progress.state.name,
                    practiceCorrectCount = progress.practiceCorrectCount,
                    challengeCorrectCount = progress.challengeCorrectCount,
                    lastAttemptTimestamp = progress.lastAttemptTimestamp
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error saving UpaSutraProgress for ${progress.id}", e)
        }
    }
}
