@file:OptIn(ExperimentalTime::class)

package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.math.round
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun getNextBirthday(month: Int, day: Int) : LocalDate? {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(timeZone)
    val currentYear: Int = today.year

    fun safeDate(year: Int, month: Int, day: Int): LocalDate {
        return try {
            LocalDate(year, month, day)
        } catch (e: IllegalArgumentException) {
            // Only Feb 29 is invalid in non-leap years
            if (month == 2 && day == 29) LocalDate(year, 3, 1)
            else throw e
        }
    }

    val birthdayThisYear = safeDate(currentYear, month, day)

    return when {
        today == birthdayThisYear -> birthdayThisYear
        today > birthdayThisYear -> safeDate(currentYear + 1, month, day)
        else -> birthdayThisYear
    }
}

fun getBirthDate(day: Int, month: Int, year: Int): LocalDate {
    return LocalDate(year, month, day)
}

@OptIn(ExperimentalTime::class)
fun getDaysFromNow(date: LocalDate): String {
    // Get today as a LocalDate in the current system time zone
    val today: LocalDate = Clock.System.todayIn(getSystemTimeZone())

    val days = today.daysUntil(date)
    val months = round(days / 30.0).toInt()

    return if (days == 0) {
        "Today!"
    } else if (days == 1) {
        "1 day"
    } else if (days < 31) {
        "$days days"
    } else {
        "$months months"
    }
}

fun getNextAge(year: Int?, month: Int, day: Int) : Int? {
    val nextBirthday = getNextBirthday(month, day)

    return if (year != null && nextBirthday != null) {
        nextBirthday.year - year
    } else {
        null
    }
}