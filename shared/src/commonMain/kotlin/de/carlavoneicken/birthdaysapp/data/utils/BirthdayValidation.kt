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

    // day
    val day = input.day.toIntOrNull() ?: return BirthdayValidationResult.Error("Day must be a number.")
    if (day !in 1..31) return BirthdayValidationResult.Error("Day must be between 1 and 31.")

    // month
    val month = input.month.toIntOrNull() ?: return BirthdayValidationResult.Error("Month must be a number.")
    if (month !in 1..12) return BirthdayValidationResult.Error("Month must be between 1 and 12.")

    // year (optional)
    val year: Int? = when {
        input.year.isNullOrBlank() -> null
        else -> {
            val y = input.year.toIntOrNull()
                ?: return BirthdayValidationResult.Error("Year must be a number.")
            if (y !in 1900..2100) {
                return BirthdayValidationResult.Error("Year must be between 1900 and 2100.")
            }
            y
        }
    }

    // convert the numbers into a LocalDate to check if it actually exists (e.g. would throw error for Feb 30)
    try {
        val dummyYear = year ?: 2000
        LocalDate(dummyYear, month, day)
    } catch (e: Exception) {
        return BirthdayValidationResult.Error("Invalid date.")
    }

    return BirthdayValidationResult.Success
}