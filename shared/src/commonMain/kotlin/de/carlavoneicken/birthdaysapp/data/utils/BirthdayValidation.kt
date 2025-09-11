package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.models.BirthdayInput
import kotlinx.datetime.LocalDate

sealed interface BirthdayValidationResult {
    object Success : BirthdayValidationResult
    data class Error(val message: String) : BirthdayValidationResult
}

fun validateBirthdayInput(input: BirthdayInput): BirthdayValidationResult {
    // check if there is a name
    if (input.name.isBlank()) return BirthdayValidationResult.Error("Name darf nicht leer sein.")

    // check if numbers were entered for day, month and year
    val day = input.day.toIntOrNull() ?: return BirthdayValidationResult.Error("Tag muss eine Zahl sein.")
    val month = input.month.toIntOrNull() ?: return BirthdayValidationResult.Error("Monat muss eine Zahl sein.")
    val year = input.year?.toIntOrNull()

    // check if the month is between 1 and 12 and the day between 1 and 31
    if (month !in 1..12) return BirthdayValidationResult.Error("Monat muss zwischen 1 und 12 sein.")
    if (day !in 1..31) return BirthdayValidationResult.Error("Tag muss zwischen 1 und 31 sein.")

    // convert the numbers into a LocalDate to check if it actually exists (e.g. would throw error for Feb 30)
    try {
        val dummyYear = year ?: 2000
        LocalDate(dummyYear, month, day)
    } catch (e: Exception) {
        return BirthdayValidationResult.Error("Ungültiges Datum.")
    }

    return BirthdayValidationResult.Success
}