package de.carlavoneicken.birthdaysapp.utils

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val birthdayFormatter: DateTimeFormatter by lazy {
    DateTimeFormatter.ofPattern("EEE, d. MMM YY", Locale.getDefault())
}

private val isoDateParser: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

fun formattedNextBirthday(birthday: Birthday): String {
    return try {
        val date: LocalDate = LocalDate.parse(birthday.nextBirthdayIso, isoDateParser)
        date.format(birthdayFormatter)
    } catch (e: Exception) {
        // Fallback just like the Swift version
        birthday.nextBirthdayIso
    }
}