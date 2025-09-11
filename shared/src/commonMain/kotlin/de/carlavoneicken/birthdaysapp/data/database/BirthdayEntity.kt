package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "birthdays")
data class BirthdayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val day: Int,
    val month: Int,
    val year: Int?
)