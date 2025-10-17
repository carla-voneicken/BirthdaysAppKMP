package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class BirthdaysTestRepository(): BirthdaysRepository {

    val sampleBirthdays = listOf(
        Birthday(id = 0, name = "Alice", day = 15, month = 1, year = 1990),
        Birthday(id = 1, name = "Bob", day = 18, month = 10, year = 1985),
        Birthday(id = 2, name = "Carol", day = 29, month = 2, year = 2000),
        Birthday(id = 3, name = "Dave", day = 31, month = 12, year = 1993),
        Birthday(id = 4, name = "Eric", day = 17, month = 10),
        Birthday(id = 5, name = "Fred", day = 29, month = 2),
        Birthday(id = 6, name = "Ava", day = 1, month = 2, year = 1992),
        Birthday(id = 7, name = "Hank", day = 10, month = 4, year = 1991),
        Birthday(id = 8, name = "Ivy", day = 12, month = 5),
        Birthday(id = 9, name = "Jake", day = 10, month = 6),
        Birthday(id = 10, name = "Kara", day = 1, month = 7),
        Birthday(id = 11, name = "Liam", day = 8, month = 8),
        Birthday(id = 12, name = "Mia", day = 1, month = 9),
        Birthday(id = 13, name = "Nina", day = 1, month = 11),
        Birthday(id = 14, name = "Owen", day = 10, month = 12)
    )

    private val _birthdays = MutableStateFlow(sampleBirthdays)

    override fun observeSingleBirthday(id: Long): Flow<Birthday?> =
        /*
        .map { list -> list.firstOrNull... } transforms stream of lists into stream of single items
        -> every time _birthdays emits a new list it runs 'list.firstOrNull {it.id == id}
        -> that finds the first Birthday with the matching id (or returns null)
        .distinctUntilChanged() -> without this the flow would emit every time the list changes, even if
        the specific birthday we care about didn't change
         */
        _birthdays.map { list -> list.firstOrNull { it.id == id } }.distinctUntilChanged()

    override fun observeBirthdaysSortedByName(): Flow<List<Birthday>> =
        _birthdays.map { list -> list.sortedBy { it.name.lowercase() } }.distinctUntilChanged()

    override fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>> =
        _birthdays.map { list -> list.sortedBy { it.nextBirthday } }.distinctUntilChanged()

    override suspend fun createBirthday(birthday: Birthday): Result<Unit> {
        // .value gets the current snapshot of the MutableStateFlow _birthdays, .toMutableList() makes
        // a mutable copy (because the original is immutable)
        val current = _birthdays.value.toMutableList()
        // determine the id of the new birthday -> if the given id is 0L or null, find the highest existing
        // id and increase by one, otherwise use the already existing valid id
        val id = if (birthday.id == 0L) (current.maxOfOrNull { it.id } ?: 0L) + 1 else birthday.id
        // add the new birthday to the list -> copy(id=id) creates a copy of the provided birthday object with the correct id value
        current += birthday.copy(id = id)
        // updates the _birthdays Flow with the new list -> .value triggers a new emission to all collectors of the Flow
        _birthdays.value = current
        return Result.success(Unit)
    }

    override suspend fun updateBirthday(birthday: Birthday): Result<Unit> {
        val current = _birthdays.value.toMutableList()
        // get the index of the first item of the list where the id matches the provided birthday's id
        // -> if no match is found, it returns '-1'
        val index = current.indexOfFirst { it.id == birthday.id }
        // as long as a match was found, update the list with the new birthday
        if (index != -1) {
            current[index] = birthday
            _birthdays.value = current
            return Result.success(Unit)
        } else {
            // otherwise return a failure
            return Result.failure(NoSuchElementException("Birthday not found: ${birthday.id}"))
        }
    }

    override suspend fun deleteBirthday(birthday: Birthday): Result<Unit> {
        // _birthdays.value creates a snapshot of the Flow -> because this list is immutable, we have to
        // substitute it with a new list instead of mutating it
        // .filterNot{} -> creates a new list containing all elements for which the given condition is FALSE
        // -> aka removes elements where the condition is true
        _birthdays.value = _birthdays.value.filterNot { it.id == birthday.id }
        return Result.success(Unit)
    }
}