package de.carlavoneicken.birthdaysapp.data.utils

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlin.jvm.JvmStatic

// to convert Kotlinx LocalDate to and from String so Room can save and read it
object LocalDateConverters {

    @TypeConverter
    @JvmStatic
    fun localDateToString(value: LocalDate?): String? =
        value?.toString()  // ISO-8601, e.g. "2025-03-14"

    @TypeConverter
    @JvmStatic
    fun stringToLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }
}