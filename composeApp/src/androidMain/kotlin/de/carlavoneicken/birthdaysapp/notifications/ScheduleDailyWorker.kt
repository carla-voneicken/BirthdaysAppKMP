package de.carlavoneicken.birthdaysapp.notifications

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun scheduleDailyWorker(context: Context, hour: Int, minute: Int) {

    val now = LocalDateTime.now()

    var nextRun = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)

    if (nextRun.isBefore(now)) {
        nextRun = nextRun.plusDays(1)
    }

    val delayMillis = Duration.between(now, nextRun).toMillis()

    val request = PeriodicWorkRequestBuilder<BirthdayReminderWorker>(
        24, TimeUnit.HOURS
    )
}