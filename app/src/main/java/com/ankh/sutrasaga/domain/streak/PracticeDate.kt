package com.ankh.sutrasaga.domain.streak

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Calendar date representation (Year, Month, Day) in local timezone.
 * Avoids simplistic millisecond-based elapsed time calculations.
 */
data class PracticeDate(
    val year: Int,
    val month: Int, // 1..12
    val day: Int    // 1..31
) : Comparable<PracticeDate> {

    init {
        require(month in 1..12) { "Month must be between 1 and 12, but was $month" }
        require(day in 1..31) { "Day must be between 1 and 31, but was $day" }
    }

    override fun compareTo(other: PracticeDate): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        return day.compareTo(other.day)
    }

    /**
     * Formats this date as ISO-8601 "YYYY-MM-DD".
     */
    fun toIsoString(): String = String.format(Locale.US, "%04d-%02d-%02d", year, month, day)

    override fun toString(): String = toIsoString()

    /**
     * Returns the preceding calendar day using UTC Calendar math to avoid DST shifts.
     */
    fun previousDay(): PracticeDate {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month - 1, day, 12, 0, 0)
            add(Calendar.DAY_OF_MONTH, -1)
        }
        return PracticeDate(
            year = cal.get(Calendar.YEAR),
            month = cal.get(Calendar.MONTH) + 1,
            day = cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    /**
     * Returns the next calendar day using UTC Calendar math to avoid DST shifts.
     */
    fun nextDay(): PracticeDate {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month - 1, day, 12, 0, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }
        return PracticeDate(
            year = cal.get(Calendar.YEAR),
            month = cal.get(Calendar.MONTH) + 1,
            day = cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    companion object {
        fun parseOrNull(isoString: String?): PracticeDate? {
            if (isoString.isNullOrBlank()) return null
            val parts = isoString.trim().split("-")
            if (parts.size != 3) return null
            val y = parts[0].toIntOrNull() ?: return null
            val m = parts[1].toIntOrNull() ?: return null
            val d = parts[2].toIntOrNull() ?: return null
            if (m !in 1..12 || d !in 1..31) return null
            return try {
                PracticeDate(y, m, d)
            } catch (e: Exception) {
                null
            }
        }

        fun fromCalendar(calendar: Calendar): PracticeDate {
            return PracticeDate(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1,
                day = calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}

/**
 * Clock abstraction for deterministic unit testing of calendar dates across timezones and dates.
 */
interface PracticeDateProvider {
    fun getTodayDate(): PracticeDate
}

/**
 * Production date provider using the device's current local timezone.
 */
class SystemPracticeDateProvider(
    private val timeZone: TimeZone = TimeZone.getDefault()
) : PracticeDateProvider {
    override fun getTodayDate(): PracticeDate {
        val calendar = Calendar.getInstance(timeZone)
        return PracticeDate.fromCalendar(calendar)
    }
}

/**
 * Authoritative streak calculation logic.
 * Single source of truth for streak transitions.
 */
object StreakCalculator {

    /**
     * Calculates the new streak count and updated last practice date string.
     *
     * Rules:
     * - First practice ever (lastPracticeDateStr == null): streak = 1
     * - Same calendar day (lastDate == today): streak unchanged
     * - Consecutive day (lastDate == today - 1 day): streak + 1
     * - Future/corrupt date (lastDate > today): safe recovery -> streak = 1
     * - Missed day (lastDate < today - 1 day): streak = 1
     */
    fun calculateNewStreak(
        currentStreak: Int,
        lastPracticeDateStr: String?,
        today: PracticeDate
    ): Pair<Int, String> {
        val todayStr = today.toIsoString()
        val lastDate = PracticeDate.parseOrNull(lastPracticeDateStr)

        if (lastDate == null) {
            return 1 to todayStr
        }

        return when {
            lastDate == today -> maxOf(1, currentStreak) to todayStr
            lastDate == today.previousDay() -> (maxOf(0, currentStreak) + 1) to todayStr
            lastDate > today -> 1 to todayStr
            else -> 1 to todayStr
        }
    }
}
