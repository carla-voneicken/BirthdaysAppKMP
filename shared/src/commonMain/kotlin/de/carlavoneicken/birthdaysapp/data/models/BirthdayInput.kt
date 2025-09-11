package de.carlavoneicken.birthdaysapp.data.models

data class BirthdayInput(
    val name: String,
    val day: String,
    val month: String,
    val year: String? = null
) {
    fun toDomain(): Birthday {
        return Birthday(
            id = 0L,
            name = name.trim(),
            day = day.toInt(),
            month = month.toInt(),
            year = year?.toInt()
        )
    }
}