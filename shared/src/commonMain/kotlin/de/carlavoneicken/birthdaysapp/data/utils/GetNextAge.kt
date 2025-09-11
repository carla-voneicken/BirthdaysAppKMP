package de.carlavoneicken.birthdaysapp.data.utils

fun getNextAge(year: Int?, month: Int, day: Int) : Int? {
    val nextBirthday = getNextBirthday(month, day)

    return if (year != null && nextBirthday != null) {
        nextBirthday.year - year
    } else {
        null
    }
}