package de.carlavoneicken.birthdaysapp.notifications

import android.content.Context

// Persistent storage for:
// - whether the worker has been scheduled
// - the time of day the worker should run
class ReminderSettings(context: Context) {
    private val prefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

    // companion object: creates one single shared place for these constants and avoids duplicating them for every instance of the class
    companion object {
        private const val KEY_WORKER_SCHEDULED = "worker_scheduled"
        private const val KEY_REMINDER_HOUR = "reminder_hour"
        private const val KEY_REMINDER_MINUTE = "reminder_minute"
        private const val DEFAULT_HOUR = 16
        private const val DEFAULT_MINUTE = 30
    }

    /*
    Custom getter and setter:
    val scheduled = settings.workerSchedules -> would call the get() function
    settings.workerScheduled = true -> would call the set() function

    p2 in the setter getBoolean() or getInt() is the default value that is returned if sharedPreferences
    do not contain a value for this key
     */
    var workerScheduled: Boolean
        get() = prefs.getBoolean(KEY_WORKER_SCHEDULED, false)
        set(value) {
            prefs.edit()
                .putBoolean(KEY_WORKER_SCHEDULED, value)
                .apply()
        }

    var reminderHour: Int
        get() = prefs.getInt(KEY_REMINDER_HOUR, DEFAULT_HOUR)
        set(value) {
            prefs.edit()
                .putInt(KEY_REMINDER_HOUR, value)
                .apply()
        }
    var reminderMinute: Int
        get() = prefs.getInt(KEY_REMINDER_MINUTE, DEFAULT_MINUTE)
        set(value) {
            prefs.edit()
                .putInt(KEY_REMINDER_MINUTE, value)
                .apply()
        }
}