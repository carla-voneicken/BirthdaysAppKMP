package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate


@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = BirthdayEntity::class,
            parentColumns = ["id"],
            childColumns = ["birthdayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("birthdayId")]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val birthdayId: Long,

    // 0 = same day, 1 = the day before, etc.
    val daysBefore: Int,

    // to avoid double-notifying if the worker runs multiple times
    val lastTriggeredDate: LocalDate? = null
)
