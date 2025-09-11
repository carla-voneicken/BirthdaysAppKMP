package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.database.BirthdayDao
import de.carlavoneicken.birthdaysapp.data.utils.toDomain
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BirthdaysRepositoryImpl(private val birthdayDao: BirthdayDao): BirthdaysRepository {

    override fun observeSingleBirthday(id: Long): Flow<Birthday?> =
        birthdayDao.observeSingleBirthday(id)
            .map { entity -> entity?.toDomain() }

    override fun observeBirthdaysSortedByName(): Flow<List<Birthday>> =
        birthdayDao.observeBirthdaysSortedByName()
            .map { entities -> entities.map { it.toDomain() } }

    override fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>> =
        birthdayDao.observeAllBirthdays()
            .map { entities -> entities.map { it.toDomain() }
                .sortedBy { it.nextBirthday } }

    override suspend fun createBirthday(birthday: Birthday): Result<Unit> {
        return try {
            birthdayDao.createBirthday(birthday.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBirthday(birthday: Birthday): Result<Unit> {
        return try {
            birthdayDao.updateBirthday(birthday.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBirthday(birthday: Birthday): Result<Unit> {
        return try {
            birthdayDao.deleteBirthday(birthday.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}