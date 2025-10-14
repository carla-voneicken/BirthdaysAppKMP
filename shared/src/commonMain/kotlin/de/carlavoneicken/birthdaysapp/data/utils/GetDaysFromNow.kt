package de.carlavoneicken.birthdaysapp.data.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
fun getDaysFromNow(date: LocalDate): String {
    // Get today as a LocalDate in the current system time zone
    val today: LocalDate = Clock.System.todayIn(getSystemTimeZone())

    val days = today.daysUntil(date)

    return when (days) {
        0 -> "Today"
        1 -> "Tomorrow"
        else -> "In ${days}\n days"
    }
}