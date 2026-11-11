package com.ank.sutrasaga.domain.models

/**
 * Domain model representing a user's practice streak state.
 */
data class PracticeStreak(
    val currentStreak: Int = 0,
    val lastPracticeDate: String? = null // ISO-8601 "YYYY-MM-DD"
)
