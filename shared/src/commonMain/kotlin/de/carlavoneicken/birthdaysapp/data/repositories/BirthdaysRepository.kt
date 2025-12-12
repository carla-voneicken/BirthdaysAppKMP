package de.carlavoneicken.birthdaysapp.data.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.BirthdayWithReminders
import de.carlavoneicken.birthdaysapp.data.models.Reminder
import kotlinx.coroutines.flow.Flow

interface BirthdaysRepository {
    @NativeCoroutines
    fun observeSingleBirthday(id: Long): Flow<Birthday?>

    @NativeCoroutines
    fun observeSingleBirthdayWithReminders(id: Long): Flow<BirthdayWithReminders?>

    @NativeCoroutines
    fun observeBirthdaysSortedByName(): Flow<List<Birthday>>

    @NativeCoroutines
    fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>>

    @NativeCoroutines
    suspend fun createBirthdayWithReminders(birthday: Birthday, reminders: List<Reminder>): Result<Unit>

    @NativeCoroutines
    suspend fun updateBirthdayWithReminders(birthday: Birthday, reminders: List<Reminder>): Result<Unit>

    @NativeCoroutines
    suspend fun deleteBirthdayById(id: Long): Result<Unit>
}