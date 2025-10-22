package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.flow.Flow

interface BirthdaysRepository {
    fun observeSingleBirthday(id: Long): Flow<Birthday?>

    fun observeBirthdaysSortedByName(): Flow<List<Birthday>>

    fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>>

    suspend fun createBirthday(birthday: Birthday): Result<Unit>

    suspend fun updateBirthday(birthday: Birthday): Result<Unit>

    suspend fun deleteBirthdayById(id: Long): Result<Unit>
}