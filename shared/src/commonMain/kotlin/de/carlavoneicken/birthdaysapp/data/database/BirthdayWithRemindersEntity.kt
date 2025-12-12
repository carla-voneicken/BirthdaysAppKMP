package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Embedded
import androidx.room.Relation

/*
SQLite performs this:

SELECT * FROM BirthdayEntity

AND for each Birthday row:
    SELECT * FROM ReminderEntity WHERE birthdayId = birthday.id

and stitches the results together into a one-to-many relationship:

BirthdayWithReminders(
    birthday = BirthdayEntity(...),
    reminders = listOf(
        ReminderEntity(...),
        ReminderEntity(...),
        ...
    )
)
 */

data class BirthdayWithRemindersEntity(
    @Embedded val birthday: BirthdayEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "birthdayId"
    )
    val reminders: List<ReminderEntity>
)