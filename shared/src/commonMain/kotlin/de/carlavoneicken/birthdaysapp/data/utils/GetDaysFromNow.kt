package de.carlavoneicken.birthdaysapp.data.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
fun getDaysFromNow(date: LocalDate): String {
    // Get today as a LocalDate in the current system time zone
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Difference in days between start-of-day(today) and start-of-day(date)
    val days = today.daysUntil(date) // positive if `date` is in the future

    return when (days) {
        0 -> "Heute"
        1 -> "Morgen"
        else -> "In ${days}\n Tagen"
    }
}