package de.carlavoneicken.birthdaysapp.data.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun formatLocalDate(date: LocalDate, pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return date.toJavaLocalDate().format(formatter)
}