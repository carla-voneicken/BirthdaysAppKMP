package de.carlavoneicken.birthdaysapp.data.models

import de.carlavoneicken.birthdaysapp.data.database.BirthdayEntity

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