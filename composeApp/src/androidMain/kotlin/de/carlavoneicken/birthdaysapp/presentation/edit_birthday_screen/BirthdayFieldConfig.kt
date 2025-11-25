package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import androidx.compose.ui.text.input.KeyboardType

enum class BirthdayFieldConfig(
    val label: String,
    val placeholder: String,
    val supportingText: String? = null,
    val keyboardType: KeyboardType = KeyboardType.Number
) {
    NAME(
        label = "Name",
        placeholder = "Enter name",
        keyboardType = KeyboardType.Text
    ),
    DAY(
        label = "Day",
        placeholder = "DD",
    ),
    MONTH(
        label = "Month",
        placeholder = "MM"
    ),
    YEAR(
        label = "Year",
        placeholder = "YYYY",
        supportingText = "Optional"
    ),
    CUSTOM_REMINDER(
        label = "Custom days before",
        placeholder = "e.g. 3"
    )
}