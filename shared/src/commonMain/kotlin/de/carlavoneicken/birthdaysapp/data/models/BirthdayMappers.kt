package de.carlavoneicken.birthdaysapp.data.models

import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel.ReminderUiState
import de.carlavoneicken.birthdaysapp.data.database.BirthdayEntity
import de.carlavoneicken.birthdaysapp.data.database.BirthdayWithRemindersEntity
import de.carlavoneicken.birthdaysapp.data.database.ReminderEntity

fun BirthdayEntity.toDomain() = Birthday(
    id = id,
    name = name,
    day = day,
    month = month,
    year = year
)

fun Birthday.toEntity() = BirthdayEntity(
    id = id,
    name = name,
    day = day,
    month = month,
    year = year
)

fun ReminderEntity.toDomain() = Reminder(
    id = id,
    birthdayId = birthdayId,
    daysBefore = daysBefore
)

fun Reminder.toEntity(): ReminderEntity {
    // make sure the birthdayId can't be null
    requireNotNull(birthdayId) { "birthdayId must not be null when saving a reminder" }

    return ReminderEntity(
        id = id,
        birthdayId = birthdayId,
        daysBefore = daysBefore
    )
}



fun BirthdayWithRemindersEntity.toDomain() = BirthdayWithReminders(
    birthday = birthday.toDomain(),
    reminders = reminders.map { it.toDomain() }
)

fun BirthdayWithReminders.toEntity() = BirthdayWithRemindersEntity(
    birthday = birthday.toEntity(),
    reminders = reminders.map { it.toEntity() }
)


fun ReminderUiState.toRemindersDomain(birthdayId: Long?): List<Reminder> {
    // if remindMe is false return an empty list
    if (!remindMe) return emptyList()

    val reminders = mutableListOf<Reminder>()

    // add the selected reminders to the list of reminders
    if (onTheDay) reminders += Reminder(birthdayId = birthdayId, daysBefore = 0)
    if (dayBefore) reminders += Reminder(birthdayId = birthdayId, daysBefore = 1)
    if (sevenDaysBefore) reminders += Reminder(birthdayId = birthdayId, daysBefore = 7)

    if (customEnabled) {
        customDays.toIntOrNull()?.let { d ->
            reminders += Reminder(birthdayId = birthdayId, daysBefore = d)
        }
    }

    return reminders
}

fun List<Reminder>.toReminderUiState(): ReminderUiState {
    // if the list is empty there are no reminders set -> remindMe = false
    if (isEmpty()) return ReminderUiState(remindMe = false)

    // any { it.daysBefore == 0 } -> does this list contain at least one reminder whose daysBefore value is 0?
    // any { predicate } -> goes through the list and returns true if the predicate is true for at least one element
    val hasOnTheDay = any { it.daysBefore == 0 }
    val hasOneDayBefore = any { it.daysBefore == 1 }
    val hasSevenDaysBefore = any { it.daysBefore == 7 }
    // returns first custom reminder - a reminder that is NOT 0, 1 or 7 - if none exist, return null
    val custom = firstOrNull { it.daysBefore !in listOf(0,1,7) }

    return ReminderUiState(
        remindMe = true,
        onTheDay = hasOnTheDay,
        dayBefore = hasOneDayBefore,
        sevenDaysBefore = hasSevenDaysBefore,
        customEnabled = custom != null,
        customDays = custom?.daysBefore?.toString().orEmpty()
    )
}
