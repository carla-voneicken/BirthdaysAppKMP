package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeBirthdaysRepository(
    initialBirthdays: List<Birthday> = emptyList()
) : BirthdaysRepository {

    // Backing in-memory list exposed as a StateFlow
    private val birthdaysFlow = MutableStateFlow(initialBirthdays)

    override fun observeSingleBirthday(id: Long): Flow<Birthday?> =
        birthdaysFlow.map { list -> list.find { it.id == id } }

    override fun observeBirthdaysSortedByName(): Flow<List<Birthday>> =
        birthdaysFlow.map { list -> list.sortedBy { it.name } }

    override fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>> =
        birthdaysFlow.map { list -> list.sortedBy { it.nextBirthday } }

    override suspend fun createBirthday(birthday: Birthday): Result<Unit> = runCatching {
        birthdaysFlow.update { it + birthday }
    }

    override suspend fun updateBirthday(birthday: Birthday): Result<Unit> = runCatching {
        birthdaysFlow.update { current ->
            current.map { if (it.id == birthday.id) birthday else it }
        }
    }

    override suspend fun deleteBirthdayById(id: Long): Result<Unit> = runCatching {
        birthdaysFlow.update { it.filterNot { b -> b.id == id } }
    }
}