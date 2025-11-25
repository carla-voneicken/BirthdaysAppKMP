package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.BirthdayWithReminders
import de.carlavoneicken.birthdaysapp.data.models.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class BirthdaysUITestRepository(): BirthdaysRepository {

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

    val sampleReminders = listOf(
        Reminder(id = 1, birthdayId = 1, daysBefore = 0),
        Reminder(id = 2, birthdayId = 1, daysBefore = 1),
        Reminder(id = 3, birthdayId = 3, daysBefore = 0),
        Reminder(id = 4, birthdayId = 4, daysBefore = 0),
        Reminder(id = 5, birthdayId = 4, daysBefore = 3),
        Reminder(id = 6, birthdayId = 6, daysBefore = 1)
    )

    private val _birthdays = MutableStateFlow(sampleBirthdays)
    private val _reminders = MutableStateFlow(sampleReminders)

    override fun observeSingleBirthday(id: Long): Flow<Birthday?> =
        /*
        .map { list -> list.firstOrNull... } transforms stream of lists into stream of single items
        -> every time _birthdays emits a new list it runs 'list.firstOrNull {it.id == id}
        -> that finds the first Birthday with the matching id (or returns null)
        .distinctUntilChanged() -> without this the flow would emit every time the list changes, even if
        the specific birthday we care about didn't change
         */
        _birthdays.map { list -> list.firstOrNull { it.id == id } }.distinctUntilChanged()

    override fun observeSingleBirthdayWithReminders(id: Long): Flow<BirthdayWithReminders?> =
        /*
        combine is a Flow operator from Kotlin coroutines -> it takes two flows, listens to both flows
        and every time EITHER of them change, it calls the lambda with their latest values
         */
        combine(_birthdays, _reminders) { birthdays, reminders ->
            /*
            birthdays.firstOrNull -> looks through the birthdays list and returns the FIRST birthday
            where the id matches (or null)
            If null is returned, return from the lambda passed to combine, not from the whole function
            -> if there is no birthday with that id, the whole combine lambda produces null and SKIPS the code below
             */
            val birthday = birthdays.firstOrNull { it.id == id } ?: return@combine null
            // reminders.filter -> goes through current list of reminders and keeps only those where the id matches
            val birthdayReminders = reminders.filter { it.birthdayId == id }
            // create a BirthdayWithReminders object
            BirthdayWithReminders(
                birthday = birthday,
                reminders = birthdayReminders
            )
            // distinctUntilChanged prevents duplicate emission -> only emits if different from previous emission
        }.distinctUntilChanged()

    override fun observeBirthdaysSortedByName(): Flow<List<Birthday>> =
        _birthdays.map { list -> list.sortedBy { it.name.lowercase() } }.distinctUntilChanged()

    override fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>> =
        _birthdays.map { list -> list.sortedBy { it.nextBirthday } }.distinctUntilChanged()

    override suspend fun createBirthdayWithReminders(
        birthday: Birthday,
        reminders: List<Reminder>
    ): Result<Unit> {
        val currentBirthdays = _birthdays.value.toMutableList()
        val currentReminders = _reminders.value.toMutableList()

        // generate new birthday id if needed
        val newBirthdayId =
            if (birthday.id == 0L) (currentBirthdays.maxOfOrNull { it.id } ?: 0L) + 1
            else birthday.id

        val birthdayWithId = birthday.copy(id = newBirthdayId)
        currentBirthdays += birthdayWithId

        // generate reminder ids
        var nextReminderId = (currentReminders.maxOfOrNull { it.id } ?: 0L) + 1
        val remindersWithIds = reminders.map { reminder ->
            reminder.copy(
                id = nextReminderId++,
                birthdayId = newBirthdayId
            )
        }
        currentReminders += remindersWithIds

        _birthdays.value = currentBirthdays
        _reminders.value = currentReminders

        return Result.success(Unit)
    }


    override suspend fun updateBirthdayWithReminders(
        birthday: Birthday,
        reminders: List<Reminder>
    ): Result<Unit> {
        val currentBirthdays = _birthdays.value.toMutableList()
        val currentReminders = _reminders.value.toMutableList()

        val index = currentBirthdays.indexOfFirst { it.id == birthday.id }
        if (index == -1) {
            return Result.failure(NoSuchElementException("Birthday not found: ${birthday.id}"))
        }

        // 1) update birthday
        currentBirthdays[index] = birthday

        // 2) remove old reminders for this birthday
        val filteredReminders = currentReminders.filterNot { it.birthdayId == birthday.id }

        // 3) add new reminders
        val mutableFiltered = filteredReminders.toMutableList()
        var nextReminderId = (mutableFiltered.maxOfOrNull { it.id } ?: 0L) + 1

        val remindersWithIds = reminders.map { reminder ->
            // if reminder.id == 0 treat as new, else keep id
            val id = if (reminder.id == 0L) nextReminderId++ else reminder.id
            reminder.copy(
                id = id,
                birthdayId = birthday.id
            )
        }
        mutableFiltered += remindersWithIds

        _birthdays.value = currentBirthdays
        _reminders.value = mutableFiltered

        return Result.success(Unit)
    }

    override suspend fun deleteBirthdayById(id: Long): Result<Unit> {
        // _birthdays.value creates a snapshot of the Flow -> because this list is immutable, we have to
        // substitute it with a new list instead of mutating it
        // .filterNot{} -> creates a new list containing all elements for which the given condition is FALSE
        // -> aka removes elements where the condition is true
        _birthdays.value = _birthdays.value.filterNot { it.id == id }
        return Result.success(Unit)
    }
}