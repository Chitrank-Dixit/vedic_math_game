package com.ankh.sutrasaga.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ankh.sutrasaga.data.db.AppDatabase
import com.ankh.sutrasaga.data.db.UpaSutraProgressDao
import com.ankh.sutrasaga.data.db.UserProgressDao
import com.ankh.sutrasaga.data.repository.GameRepository
import com.ankh.sutrasaga.domain.models.UpaSutraCompletionState
import com.ankh.sutrasaga.domain.models.UpaSutraId
import com.ankh.sutrasaga.domain.models.UpaSutraProgress
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
class GameRepositoryFullPersistenceTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserProgressDao
    private lateinit var upaDao: UpaSutraProgressDao
    private lateinit var repository: GameRepository

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = database.userProgressDao()
        upaDao = database.upaSutraProgressDao()
        repository = GameRepository(userDao, upaDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testUpaSutraPersistenceAndRetrieval() = runBlocking {
        val progress = UpaSutraProgress(
            id = UpaSutraId.ANTYAYORDASHAKEPI,
            state = UpaSutraCompletionState.MASTERED,
            practiceCorrectCount = 5,
            challengeCorrectCount = 3,
            lastAttemptTimestamp = 123456789L
        )

        repository.saveUpaSutraProgress(progress)

        val retrieved = repository.getUpaSutraProgress(UpaSutraId.ANTYAYORDASHAKEPI)
        assertNotNull(retrieved)
        assertEquals(UpaSutraId.ANTYAYORDASHAKEPI, retrieved?.id)
        assertEquals(UpaSutraCompletionState.MASTERED, retrieved?.state)
        assertEquals(5, retrieved?.practiceCorrectCount)
        assertEquals(3, retrieved?.challengeCorrectCount)

        val all = repository.getAllUpaSutraProgress().first()
        assertEquals(1, all.size)
        assertEquals(UpaSutraId.ANTYAYORDASHAKEPI, all[0].id)
    }

    @Test
    fun testFreshInstallDefaults() = runBlocking {
        val userProgress = repository.getAllProgress().first()
        assertTrue(userProgress.isEmpty())

        val upaProgress = repository.getAllUpaSutraProgress().first()
        assertTrue(upaProgress.isEmpty())
    }
}
