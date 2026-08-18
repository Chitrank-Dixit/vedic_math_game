package com.ankh.sutrasaga.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upa_sutra_progress")
data class UpaSutraProgressEntity(
    @PrimaryKey val upaSutraId: String,
    val state: String,
    val practiceCorrectCount: Int,
    val challengeCorrectCount: Int,
    val lastAttemptTimestamp: Long
)
