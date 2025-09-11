@file:OptIn(ExperimentalTime::class)

package de.carlavoneicken.birthdaysapp.data.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun getNextBirthday(month: Int, day: Int) : LocalDate? {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(timeZone)
    val currentYear: Int = today.year

    // create LocalDate for the birthday this year (catch errors, e.g. Feb 29th in a non-leap year)
    val birthdayThisYear = try {
        LocalDate(currentYear, month, day)
    } catch (e: IllegalArgumentException) {
        return null
    }

    return when {
        // if birthday is TODAY, show birthdayThisYear as next birthday
        today == birthdayThisYear -> birthdayThisYear
        // if today is after birthday (birthday already happened this year) add a year to it
        today > birthdayThisYear -> try {
            LocalDate(currentYear + 1, month, day)
        } catch (e: IllegalArgumentException) {
            null
        }
        // else (birthday still coming this year) return birthdayThisYear
        else -> birthdayThisYear
    }
}

