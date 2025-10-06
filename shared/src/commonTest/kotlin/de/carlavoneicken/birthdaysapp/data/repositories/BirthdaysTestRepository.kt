package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.database.sampleBirthdays
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.withLock


/*
MUTEX -> mutual exclusion lock
ensure only one coroutine at a time can execute a given block of code

Because the repository has a MutableStateFlow _birthdays which holds a shared mutable list that can be updated
from multiple coroutines, we need to avoid two actions being taken on the list at the same time

mutex.withLock {} -> only one coroutine can execute that block at a time, other calls to withLock will
suspend until the current one finishes

In the real repository implementation we don't need this because Room already handles concurrency for us
*/


class BirthdaysTestRepository(
    initial: List<Birthday> = sampleBirthdays
): BirthdaysRepository {

    private val mutex = Mutex()
    private val _birthdays = MutableStateFlow(initial.toList())

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

    override suspend fun createBirthday(birthday: Birthday): Result<Unit> = mutex.withLock {
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
        Result.success(Unit)
    }

    override suspend fun updateBirthday(birthday: Birthday): Result<Unit> = mutex.withLock {
        val current = _birthdays.value.toMutableList()
        // get the index of the first item of the list where the id matches the provided birthday's id
        // -> if no match is found, it returns '-1'
        val index = current.indexOfFirst { it.id == birthday.id }
        // as long as a match was found, update the list with the new birthday
        if (index != -1) {
            current[index] = birthday
            _birthdays.value = current
            Result.success(Unit)
        } else {
            // otherwise return a failure
            Result.failure(NoSuchElementException("Birthday not found: ${birthday.id}"))
        }
    }

    override suspend fun deleteBirthday(birthday: Birthday): Result<Unit> = mutex.withLock {
        // _birthdays.value creates a snapshot of the Flow -> because this list is immutable, we have to
        // substitute it with a new list instead of mutating it
        // .filterNot{} -> creates a new list containing all elements for which the given condition is FALSE
        // -> aka removes elements where the condition is true
        _birthdays.value = _birthdays.value.filterNot { it.id == birthday.id }
        Result.success(Unit)
    }
}