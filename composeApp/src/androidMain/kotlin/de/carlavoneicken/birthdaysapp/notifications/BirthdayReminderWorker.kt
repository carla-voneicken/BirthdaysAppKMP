package de.carlavoneicken.birthdaysapp.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.carlavoneicken.birthdaysapp.data.database.BirthdayDao
import de.carlavoneicken.birthdaysapp.data.database.BirthdayEntity
import de.carlavoneicken.birthdaysapp.data.database.ReminderDao
import de.carlavoneicken.birthdaysapp.data.database.ReminderEntity
import de.carlavoneicken.birthdaysapp.data.utils.getNextBirthday
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.minus

class BirthdayReminderWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params), KoinComponent {

    private val birthdayDao: BirthdayDao by inject()
    private val reminderDao: ReminderDao by inject()

    @OptIn(ExperimentalTime::class)
    override suspend fun doWork(): Result {
        val today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        // get list of all birthdays with their related reminders
        val birthdaysWithReminders = birthdayDao.getAllBirthdaysWithReminders()

        // check all birthdays
        birthdaysWithReminders.forEach { birthdayWithReminder ->
            val birthday = birthdayWithReminder.birthday
            // check all reminders for each birthday if they should be triggered today
            birthdayWithReminder.reminders
                .forEach { reminder ->
                    if (shouldTriggerReminder(today, birthday, reminder)) {
                        showBirthdayNotification(applicationContext, birthday)
                        reminderDao.updateLastTriggeredData(reminder.id, today)
                    }
                }
        }
        return Result.success()
    }
}

private fun shouldTriggerReminder(
    today: LocalDate,
    birthday: BirthdayEntity,
    reminder: ReminderEntity
): Boolean {
    // shouldn't trigger if it was already triggered today
    if (reminder.lastTriggeredDate == today) return false

    // get the next birthdate and the date for the reminder by subtracting how many days before the user wants to be reminded
    val nextBirthday = getNextBirthday(birthday.month, birthday.day)
    val reminderDate = nextBirthday.minus(DatePeriod(days = reminder.daysBefore))

    // if the reminderDate is today, then yes, the reminder should be triggered
    return reminderDate == today
}