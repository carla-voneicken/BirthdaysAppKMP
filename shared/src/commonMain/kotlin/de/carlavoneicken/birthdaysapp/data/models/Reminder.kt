package de.carlavoneicken.birthdaysapp.data.models

import kotlinx.datetime.LocalDate

data class Reminder(
    val id: Long = 0L,
    val birthdayId: Long? = null, // null for new birthdays, filled in by usecase/repo
    val daysBefore: Int,
    val lastTriggeredDate: LocalDate? = null
)