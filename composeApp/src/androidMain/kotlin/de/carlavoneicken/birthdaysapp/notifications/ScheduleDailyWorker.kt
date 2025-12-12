package de.carlavoneicken.birthdaysapp.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun scheduleDailyWorker(
    context: Context,       // Android context used to access WorkManager
    hour: Int = 8,          // hour of the day when the worker should first run
    minute: Int = 0         // minute of that hour
) {
    // get current date and time
    val now = LocalDateTime.now()

    // build LocalDateTime representing "today at [hour]:[minute]"
    var nextRun = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)

    // if time today has already passed, schedule for same time tomorrow instead
    if (nextRun.isBefore(now)) {
        nextRun = nextRun.plusDays(1)
    }

    // WorkManager needs a delay in milliseconds, not a LocalDateTime -> calculate the delay between
    // now and the next run time (in milliseconds)
    val delayMillis = Duration.between(now, nextRun).toMillis()

    // build a Periodic WorkRequest that
    // - runs the BirthdayReminderWorker
    // - repeats every 24 hours
    // - waits 'delayMillis' before FIRST run
    val request = PeriodicWorkRequestBuilder<BirthdayReminderWorker>(
        24, TimeUnit.HOURS // repeat interval: once every 24 hours
    )
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)    // delay until first run
        .addTag("birthday_reminder")                                // tag for debugging
        .build()

    // Enqueue the PeriodicWorkRequest as UNIQUE background work
    // If a worker with the same name already exists, UPDATE replaces it
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "BirthdayReminderWorker",      // unique name so only ONE worker runs
        ExistingPeriodicWorkPolicy.UPDATE,              // replace old worker with new one
        request
    )
}