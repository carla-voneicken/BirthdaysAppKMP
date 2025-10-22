package de.carlavoneicken.birthdaysapp.data.utils

// iosMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import platform.Foundation.*

private fun LocalDate.toNSDate(): NSDate {
    val comps = NSDateComponents()
    comps.year = this.year.toLong()
    comps.month = this.month.number.toLong()
    comps.day = this.day.toLong()
    val calendar = NSCalendar.currentCalendar
    return calendar.dateFromComponents(comps)!!
}

actual fun formatLocalDate(date: LocalDate, pattern: String): String {
    val f = NSDateFormatter()
    f.locale = NSLocale.currentLocale
    f.timeZone = NSTimeZone.localTimeZone
    f.dateFormat = pattern
    return f.stringFromDate(date.toNSDate())
}