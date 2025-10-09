package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.models.Birthday

fun createPreviewBirthday(
    id: Long = 1,
    name: String = "Shannon Cruz",
    day: Int = 4,
    month: Int = 10,
    year: Int? = 2021
) = Birthday(
    id = id,
    name = name,
    day = day,
    month = month,
    year = year
)