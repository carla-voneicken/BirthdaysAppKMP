package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.models.BirthdayInput
import kotlinx.datetime.LocalDate

sealed interface BirthdayValidationResult {
    object Success : BirthdayValidationResult
    data class Error(val message: String) : BirthdayValidationResult
}

fun validateBirthdayInput(input: BirthdayInput): BirthdayValidationResult {
    // check if there is a name
    if (input.name.isBlank()) return BirthdayValidationResult.Error("Name can't be empty.")

    // check if numbers were entered for day, month and year
    val day = input.day.toIntOrNull() ?: return BirthdayValidationResult.Error("Day must be a number.")
    val month = input.month.toIntOrNull() ?: return BirthdayValidationResult.Error("Month must be a number.")
    val year = input.year?.toIntOrNull()

    // check if the month is between 1 and 12 and the day between 1 and 31
    if (month !in 1..12) return BirthdayValidationResult.Error("Month must be between 1 and 12.")
    if (day !in 1..31) return BirthdayValidationResult.Error("Day must be between 1 and 31.")

    // convert the numbers into a LocalDate to check if it actually exists (e.g. would throw error for Feb 30)
    try {
        val dummyYear = year ?: 2000
        LocalDate(dummyYear, month, day)
    } catch (e: Exception) {
        return BirthdayValidationResult.Error("Invalid date.")
    }

    return BirthdayValidationResult.Success
}