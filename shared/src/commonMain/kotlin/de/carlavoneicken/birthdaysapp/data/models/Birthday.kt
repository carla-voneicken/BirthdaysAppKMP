package de.carlavoneicken.birthdaysapp.data.models

import de.carlavoneicken.birthdaysapp.data.utils.getNextAge
import de.carlavoneicken.birthdaysapp.data.utils.getNextBirthday
import de.carlavoneicken.birthdaysapp.data.utils.getZodiacSign
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Birthday(
    val id: Long = 0L,
    val name: String,
    val day: Int,
    val month: Int,
    val year: Int? = null
) {
    val zodiacSign: ZodiacSign?
        get() = getZodiacSign(month, day)

    val nextBirthday: LocalDate
        get() = getNextBirthday(month, day) ?: Clock.System.todayIn(TimeZone.currentSystemDefault())

    val nextAge: Int?
        get() = getNextAge(year, month, day)
}


