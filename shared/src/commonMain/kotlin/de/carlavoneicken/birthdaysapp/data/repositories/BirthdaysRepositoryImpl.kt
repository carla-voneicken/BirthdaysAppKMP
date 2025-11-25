package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.database.BirthdayDao
import de.carlavoneicken.birthdaysapp.data.database.ReminderDao
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.BirthdayWithReminders
import de.carlavoneicken.birthdaysapp.data.models.Reminder
import de.carlavoneicken.birthdaysapp.data.models.toDomain
import de.carlavoneicken.birthdaysapp.data.models.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BirthdaysRepositoryImpl(): BirthdaysRepository, KoinComponent {

    private val birthdayDao: BirthdayDao by inject()
    private val reminderDao: ReminderDao by inject()

    override fun observeSingleBirthday(id: Long): Flow<Birthday?> =
        birthdayDao.observeSingleBirthday(id)
            .map { entity -> entity?.toDomain() }

    override fun observeSingleBirthdayWithReminders(id: Long): Flow<BirthdayWithReminders?> =
        birthdayDao.observeBirthdayWithReminders(id)
            .map { entity -> entity?.toDomain() }

    // database handles the sorting by name
    override fun observeBirthdaysSortedByName(): Flow<List<Birthday>> =
        birthdayDao.observeBirthdaysSortedByName()
            .map { entities -> entities.map { it.toDomain() } }

    // database only returns list of birthdays, after mapping them .toDomain we can sort them by upcoming date
    override fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>> =
        birthdayDao.observeAllBirthdays()
            .map { entities -> entities
                .map { it.toDomain() }
                .sortedBy { it.nextBirthday } }

    override suspend fun createBirthdayWithReminders(
        birthday: Birthday,
        reminders: List<Reminder>
    ): Result<Unit> {
        return try {
                // Insert birthday, get new id
                val newId = birthdayDao.createBirthday(birthday.toEntity())

                // Insert reminders with that id
                if (reminders.isNotEmpty()) {
                    val entities = reminders.map {
                        // birthdayId might be null in domain; ensure it's set here
                        it.copy(birthdayId = newId).toEntity()
                    }
                    reminderDao.insertAll(entities)
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBirthdayWithReminders(
        birthday: Birthday,
        reminders: List<Reminder>
    ): Result<Unit> {
        return try {
            birthdayDao.updateBirthday(birthday.toEntity())

            val id = birthday.id

            reminderDao.deleteByBirthdayId(id)

            if (reminders.isNotEmpty()) {
                val entities = reminders.map {
                    it.copy(birthdayId = id).toEntity()
                }
                reminderDao.insertAll(entities)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteBirthdayById(id: Long): Result<Unit> {
        return try {
            birthdayDao.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}