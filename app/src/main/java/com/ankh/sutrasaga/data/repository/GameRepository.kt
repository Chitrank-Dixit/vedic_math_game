package com.ankh.sutrasaga.data.repository

import android.util.Log
import com.ankh.sutrasaga.data.db.PracticeStreakDao
import com.ankh.sutrasaga.data.db.PracticeStreakEntity
import com.ankh.sutrasaga.data.db.UpaSutraProgressDao
import com.ankh.sutrasaga.data.db.UpaSutraProgressEntity
import com.ankh.sutrasaga.data.db.UserProgressDao
import com.ankh.sutrasaga.data.db.UserProgressEntity
import com.ankh.sutrasaga.domain.models.PracticeStreak
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraProgress
import com.ankh.sutrasaga.domain.streak.PracticeDate
import com.ankh.sutrasaga.domain.streak.PracticeDateProvider
import com.ankh.sutrasaga.domain.streak.StreakCalculator
import com.ankh.sutrasaga.domain.streak.SystemPracticeDateProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GameRepository(
    private val userDao: UserProgressDao,
    private val upaDao: UpaSutraProgressDao? = null,
    private val streakDao: PracticeStreakDao? = null,
    private val dateProvider: PracticeDateProvider = SystemPracticeDateProvider()
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
        val validatedScore = score.coerceAtLeast(0)
        try {
            val existing = userDao.getProgressForWorld(worldId)
            val bestScore = if (existing != null) maxOf(existing.bestScore, validatedScore) else validatedScore
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

    /**
     * Observes reactive practice streak state from Room database.
     * Emits default 0 streak with null date if no record has been created yet.
     */
    fun getPracticeStreak(): Flow<PracticeStreak> {
        return streakDao?.getStreakFlow()?.map { entity ->
            if (entity != null) {
                PracticeStreak(
                    currentStreak = entity.currentStreak,
                    lastPracticeDate = entity.lastPracticeDate
                )
            } else {
                PracticeStreak(currentStreak = 0, lastPracticeDate = null)
            }
        } ?: flowOf(PracticeStreak(currentStreak = 0, lastPracticeDate = null))
    }

    /**
     * Fetches snapshot of current practice streak.
     */
    suspend fun getPracticeStreakSnapshot(): PracticeStreak {
        val entity = streakDao?.getStreak()
        return if (entity != null) {
            PracticeStreak(
                currentStreak = entity.currentStreak,
                lastPracticeDate = entity.lastPracticeDate
            )
        } else {
            PracticeStreak(currentStreak = 0, lastPracticeDate = null)
        }
    }

    /**
     * Single authoritative practice streak update operation.
     * Evaluates calendar day transition rules and persists updated streak into Room.
     */
    suspend fun recordPracticeDay(today: PracticeDate = dateProvider.getTodayDate()): PracticeStreak {
        val currentEntity = streakDao?.getStreak()
        val currentStreak = currentEntity?.currentStreak ?: 0
        val lastPracticeDate = currentEntity?.lastPracticeDate

        val (newStreak, newDateStr) = StreakCalculator.calculateNewStreak(
            currentStreak = currentStreak,
            lastPracticeDateStr = lastPracticeDate,
            today = today
        )

        val updatedEntity = PracticeStreakEntity(
            id = 1,
            currentStreak = newStreak,
            lastPracticeDate = newDateStr
        )

        try {
            streakDao?.saveStreak(updatedEntity)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving practice streak", e)
        }

        return PracticeStreak(
            currentStreak = newStreak,
            lastPracticeDate = newDateStr
        )
    }
}

