package de.carlavoneicken.birthdaysapp.data.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.math.round
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
fun getDaysFromNow(date: LocalDate): String {
    // Get today as a LocalDate in the current system time zone
    val today: LocalDate = Clock.System.todayIn(getSystemTimeZone())

    val days = today.daysUntil(date)
    val months = round(days / 30.0).toInt()

    return if (days == 0) {
        "Today!"
    } else if (days == 1) {
        "Tomorrow"
    } else if (days < 31) {
        "$days days"
    } else {
        "$months months"
    }
}