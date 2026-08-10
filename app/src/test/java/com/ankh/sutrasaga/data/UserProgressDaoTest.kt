package com.ankh.sutrasaga.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ankh.sutrasaga.data.db.AppDatabase
import com.ankh.sutrasaga.data.db.UserProgressDao
import com.ankh.sutrasaga.data.db.UserProgressEntity
import com.ankh.sutrasaga.data.repository.GameRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProgressDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: UserProgressDao
    private lateinit var repository: GameRepository

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.userProgressDao()
        repository = GameRepository(dao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testSaveAndGetWorldProgress() = runBlocking {
        val entity = UserProgressEntity(
            worldId = 1,
            isUnlocked = true,
            isCompleted = true,
            bestScore = 450
        )

        dao.saveProgress(entity)

        val retrieved = dao.getProgressForWorld(1)
        assertNotNull(retrieved)
        assertEquals(1, retrieved?.worldId)
        assertTrue(retrieved?.isCompleted == true)
        assertEquals(450, retrieved?.bestScore)
    }

    @Test
    fun testRepositorySaveWorldCompletionHigherScore() = runBlocking {
        repository.saveWorldCompletion(worldId = 1, score = 300)
        var progress = dao.getProgressForWorld(1)
        assertEquals(300, progress?.bestScore)

        // Save higher score
        repository.saveWorldCompletion(worldId = 1, score = 500)
        progress = dao.getProgressForWorld(1)
        assertEquals(500, progress?.bestScore)

        // Lower score should not overwrite best score
        repository.saveWorldCompletion(worldId = 1, score = 200)
        progress = dao.getProgressForWorld(1)
        assertEquals(500, progress?.bestScore)
    }

    @Test
    fun testGetAllProgressFlowEmissions() = runBlocking {
        repository.saveWorldCompletion(worldId = 1, score = 400)
        repository.saveWorldCompletion(worldId = 2, score = 450)

        val list = repository.getAllProgress().first()
        assertEquals(2, list.size)
        assertTrue(list.any { it.worldId == 1 && it.bestScore == 400 })
        assertTrue(list.any { it.worldId == 2 && it.bestScore == 450 })
    }
}
