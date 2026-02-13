package de.carlavoneicken.birthdaysapp.data.repositories

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.BirthdayWithReminders
import de.carlavoneicken.birthdaysapp.data.models.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/*runCatching {
    birthdaysFlow.update { it + birthday }
}

 DAS GLEICHE WIE:

try {
    birthdaysFlow.update { it + birthday }
    Result.success(Unit)
} catch (e: Exception) {
    Result.failure(e)
}
 */
class FakeBirthdaysRepository() : BirthdaysRepository {

    var sampleBirthdays: List<Birthday> = listOf(
        Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993),
        Birthday(id = 2, name = "Lauren", day = 31, month = 12, year = 1992),
        Birthday(id = 3, name = "Stine", day = 22, month = 2, year = 1993),
        Birthday(id = 4, name = "Lene", day = 18, month = 8, year = 1992)
    )

    // Sample reminders (linked to birthdays by birthdayId)
    private val sampleReminders = listOf(
        Reminder(id = 1, birthdayId = 1, daysBefore = 0, lastTriggeredDate = null),  // For Lina
        Reminder(id = 2, birthdayId = 1, daysBefore = 1, lastTriggeredDate = null),  // For Lina
        Reminder(id = 3, birthdayId = 2, daysBefore = 1, lastTriggeredDate = null),  // For Lauren
    )

    // Backing in-memory list exposed as a StateFlow
    private val birthdaysFlow = MutableStateFlow(sampleBirthdays)
    private val remindersFlow = MutableStateFlow(sampleReminders)
    var shouldThrowOnCreate = false

    // clear saved birthdays to test the result for empty lists
    fun clear() {
        birthdaysFlow.value = emptyList()
        remindersFlow.value = emptyList()
    }

    override fun observeSingleBirthday(id: Long): Flow<Birthday?> =
        birthdaysFlow.map { list -> list.find { it.id == id } }

    override fun observeSingleBirthdayWithReminders(id: Long): Flow<BirthdayWithReminders?> =
        combine(birthdaysFlow, remindersFlow) { birthdays, reminders ->
            val birthday = birthdays.find { it.id == id}
            if (birthday == null) {
                null
            } else {
                val birthdayReminders = reminders.filter { it.birthdayId == id }
                BirthdayWithReminders(
                    birthday = birthday,
                    reminders = birthdayReminders
                )
            }
        }


    override fun observeBirthdaysSortedByName(): Flow<List<Birthday>> =
        birthdaysFlow.map { list -> list.sortedBy { it.name } }

    override fun observeBirthdaysSortedByUpcoming(): Flow<List<Birthday>> =
        birthdaysFlow.map { list -> list.sortedBy { it.nextBirthday } }

    // runCatching -> captures exceptions and wraps the result in a Result object (same as try-catch)
    override suspend fun createBirthdayWithReminders(
        birthday: Birthday,
        reminders: List<Reminder>
    ): Result<Unit> = runCatching {
        // if shouldThrowOnCreate is true, return an error
        if (shouldThrowOnCreate) error("Simulated database error")
        // .update is a thread-safe operation on a MutableStateFlow
        // -> lambda receives current value (it) which is the current List<Birthday>
        // it + birthday -> creates a new list that contains all existing items plus the new one
        birthdaysFlow.update { it + birthday }
        if (reminders.isNotEmpty()) {
            // iterating over the list of reminders that should be saved, setting the birthdayId for
            // each of them to relate them to the birthday, then adding all the reminders to the remindersFlow
            val remindersWithId = reminders.map { it.copy(birthdayId = birthday.id) }
            remindersFlow.update { it + remindersWithId }
        }
    }

    override suspend fun updateBirthdayWithReminders(
        birthday: Birthday,
        reminders: List<Reminder>
    ): Result<Unit> = runCatching {
        // if shouldThrowOnCreate is true, return an error
        if (shouldThrowOnCreate) error("Simulated database error")

        // Update birthday
        /* .update is a thread-safe operation on a MutableStateFlow -> lambda receives current value
        which is the current List<Birthday> */
        birthdaysFlow.update { currentBirthdayList ->
            // iterate over the current list of Birthdays and transform each element -> if the id matches
            // the birthday id, set it to the new birthday, otherwise set it to itself (aka don't change it)
            currentBirthdayList.map { if (it.id == birthday.id) birthday else it }
        }

        // Delete old reminders for this birthday by filtering them out
        remindersFlow.update { currentRemindersList ->
            currentRemindersList.filterNot { it.birthdayId == birthday.id }
        }

        // Add new reminders
        if (reminders.isNotEmpty()) {
            // iterating over the list of reminders that should be saved, setting the birthdayId for
            // each of them to relate them to the birthday, then adding all the reminders to the remindersFlow
            val remindersWithId = reminders.map { it.copy(birthdayId = birthday.id) }
            remindersFlow.update { it + remindersWithId }
        }
    }


    override suspend fun deleteBirthdayById(id: Long): Result<Unit> = runCatching {
        // if shouldThrowOnCreate is true, return an error
        if (shouldThrowOnCreate) error("Simulated database error")
        // it.filterNot filters out the birthday item and it's reminders where the id matches the specified id
        birthdaysFlow.update { it.filterNot { b -> b.id == id } }
        remindersFlow.update { it.filterNot { r -> r.birthdayId == id } }
    }
}