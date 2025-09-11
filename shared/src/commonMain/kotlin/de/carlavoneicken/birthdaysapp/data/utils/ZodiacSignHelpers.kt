package de.carlavoneicken.birthdaysapp.data.utils

import de.carlavoneicken.birthdaysapp.data.models.ZodiacSign
import kotlinx.datetime.LocalDate

fun getZodiacSign(month: Int, day: Int): ZodiacSign? {
// Each range uses a "dummy" year 2000 because we only care about month/day
    val zodiacRanges = listOf(
        Triple(LocalDate(2000, 12, 22), LocalDate(2000, 1, 19), ZodiacSign.CAPRICORN),
        Triple(LocalDate(2000, 1, 20), LocalDate(2000, 2, 18), ZodiacSign.AQUARIUS),
        Triple(LocalDate(2000, 2, 19), LocalDate(2000, 3, 20), ZodiacSign.PISCES),
        Triple(LocalDate(2000, 3, 21), LocalDate(2000, 4, 19), ZodiacSign.ARIES),
        Triple(LocalDate(2000, 4, 20), LocalDate(2000, 5, 20), ZodiacSign.TAURUS),
        Triple(LocalDate(2000, 5, 21), LocalDate(2000, 6, 20), ZodiacSign.GEMINI),
        Triple(LocalDate(2000, 6, 21), LocalDate(2000, 7, 22), ZodiacSign.CANCER),
        Triple(LocalDate(2000, 7, 23), LocalDate(2000, 8, 22), ZodiacSign.LEO),
        Triple(LocalDate(2000, 8, 23), LocalDate(2000, 9, 22), ZodiacSign.VIRGO),
        Triple(LocalDate(2000, 9, 23), LocalDate(2000, 10, 22), ZodiacSign.LIBRA),
        Triple(LocalDate(2000, 10, 23), LocalDate(2000, 11, 21), ZodiacSign.SCORPIO),
        Triple(LocalDate(2000, 11, 22), LocalDate(2000, 12, 21), ZodiacSign.SAGITTARIUS)
    )

    val birthday = LocalDate(2000, month, day) // dummy year 2000

    for ((start, end, sign) in zodiacRanges) {
        if (isBirthdayInRange(birthday, start, end)) {
            return sign
        }
    }
    return null
}

private fun isBirthdayInRange(birthday: LocalDate, startDate: LocalDate, endDate: LocalDate) : Boolean {
    // Special handling for Capricorn as its range crosses the year boundary
    return if (startDate > endDate) {
        // capricorn: startDate = 22.12., endDate = 19.1. -> birthday is in range when it's larger than
        // the startDate (birthdays in December) or smaller than the endDate (birthdays in January)
        birthday >= startDate || birthday <= endDate
    } else {
        // birthday is in range when it's larger/equal to the startDate and smaller/equal to the endDate
        birthday >= startDate && birthday <= endDate
    }
}