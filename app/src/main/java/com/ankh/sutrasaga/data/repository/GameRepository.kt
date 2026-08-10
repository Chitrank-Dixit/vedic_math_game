package com.ankh.sutrasaga.data.repository

import com.ankh.sutrasaga.data.db.UserProgressDao
import com.ankh.sutrasaga.data.db.UserProgressEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: UserProgressDao) {

    fun getAllProgress(): Flow<List<UserProgressEntity>> = dao.getAllProgress()

    suspend fun getProgressForWorld(worldId: Int): UserProgressEntity? {
        return dao.getProgressForWorld(worldId)
    }

    suspend fun saveWorldCompletion(worldId: Int, score: Int) {
        val existing = dao.getProgressForWorld(worldId)
        val bestScore = if (existing != null) maxOf(existing.bestScore, score) else score
        dao.saveProgress(
            UserProgressEntity(
                worldId = worldId,
                isUnlocked = true,
                isCompleted = true,
                bestScore = bestScore
            )
        )
    }
}
