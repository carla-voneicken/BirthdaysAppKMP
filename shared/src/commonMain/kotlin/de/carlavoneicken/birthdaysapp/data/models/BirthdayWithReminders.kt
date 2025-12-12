package de.carlavoneicken.birthdaysapp.data.models

data class BirthdayWithReminders(
    val birthday: Birthday,
    val reminders: List<Reminder>
)