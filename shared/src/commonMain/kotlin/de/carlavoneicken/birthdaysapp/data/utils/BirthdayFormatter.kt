package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.datetime.LocalDate

expect fun formatLocalDate(date: LocalDate, pattern: String): String

fun formattedBirthDate(birthday: Birthday): String {
    val date = LocalDate(year = (birthday.year ?: 2000), month = birthday.month, day = birthday.day)
    val pattern = if (birthday.year != null) "d. MMMM yyyy" else "d. MMMM"
    return formatLocalDate(date, pattern) // expect → actual per platform
}

fun formattedNextBirthday(birthday: Birthday, pattern: String = "EEE, d. MMM yy"): String {
    // nextBirthdayIso is "yyyy-MM-dd"
    val next = try {
        LocalDate.parse(birthday.nextBirthdayIso)
    } catch (_: Exception) {
        return birthday.nextBirthdayIso // fallback
    }
    return formatLocalDate(next, pattern)
}