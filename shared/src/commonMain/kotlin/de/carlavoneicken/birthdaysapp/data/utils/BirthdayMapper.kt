package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.database.BirthdayEntity
import de.carlavoneicken.birthdaysapp.data.models.Birthday

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