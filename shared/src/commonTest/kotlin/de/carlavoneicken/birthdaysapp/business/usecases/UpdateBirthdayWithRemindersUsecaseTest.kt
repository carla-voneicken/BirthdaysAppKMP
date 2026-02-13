package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import assertk.assertions.isEqualTo
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.Reminder
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import de.carlavoneicken.birthdaysapp.di.BaseKoinTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateBirthdayWithRemindersUsecaseTest: BaseKoinTest() {

    private val repo: BirthdaysRepository by inject<BirthdaysRepository>()
    private var usecase = UpdateBirthdayWithRemindersUsecase()

    @Test
    fun `updateBirthday returns success`() = runTest {
        // Act
        val result = usecase(
            Birthday(0L, "Alice", 1, 3, 1990),
            listOf(
                Reminder(id = 0L, birthdayId = 0L, daysBefore = 0, lastTriggeredDate = null),
                Reminder(id = 0L, birthdayId = 0L, daysBefore = 1, lastTriggeredDate = null)
            )
        )

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateBirthday returns failure when repo throws`() = runTest {
        // Arrange
        // casting the repository so the test knows it's actually a FakeBirthdaysRepository
        // (the others don't have the shouldThrowOnCreate variable)
        val fakeRepo = repo as FakeBirthdaysRepository
        fakeRepo.shouldThrowOnCreate = true   // simulate DB exception

        // Act
        val result = usecase(
            Birthday(1, "Alice", 1, 3, 1990),
            emptyList()
        )

        // Assert
        assertTrue(result.isFailure)
        assertThat("Simulated database error").equals(result.exceptionOrNull()?.message)
    }

    @Test
    fun `updateBirthday actually updates the birthday data`() = runTest {
        // Arrange: Birthday 1 (Lina) exists with original data
        val originalBirthday = repo.observeSingleBirthday(1).first()
        assertThat(originalBirthday?.name).isEqualTo("Lina")

        // Act: Update it
        val result = usecase(
            Birthday(id = 1, name = "Lina Updated", day = 15, month = 5, year = 1993),
            emptyList()
        )

        // Assert: Data actually changed
        assertTrue(result.isSuccess)
        val updatedBirthday = repo.observeSingleBirthday(1).first()
        assertThat(updatedBirthday?.name).isEqualTo("Lina Updated")
        assertThat(updatedBirthday?.day).isEqualTo(15)
        assertThat(updatedBirthday?.month).isEqualTo(5)
    }

    @Test
    fun `updateBirthday replaces old reminders with new ones`() = runTest {
        // Arrange: Birthday 1 has 2 reminders (from sample data)
        val originalData = repo.observeSingleBirthdayWithReminders(1).first()
        assertThat(originalData?.reminders?.size).isEqualTo(2)

        // Act: Update with 3 new reminders
        val result = usecase(
            Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993),
            listOf(
                Reminder(id = 10L, birthdayId = 1L, daysBefore = 0, lastTriggeredDate = null),
                Reminder(id = 11L, birthdayId = 1L, daysBefore = 5, lastTriggeredDate = null),
                Reminder(id = 12L, birthdayId = 1L, daysBefore = 7, lastTriggeredDate = null)
            )
        )

        // Assert: Now has 3 reminders, old ones are gone
        assertTrue(result.isSuccess)
        val updatedData = repo.observeSingleBirthdayWithReminders(1).first()
        assertThat(updatedData?.reminders?.size).isEqualTo(3)
        assertEquals(updatedData?.reminders?.any { it.daysBefore == 0 }, true)
        assertEquals(updatedData?.reminders?.any { it.daysBefore == 1 }, false)
        assertEquals(updatedData?.reminders?.any { it.daysBefore == 5 }, true)
        assertEquals(updatedData?.reminders?.any { it.daysBefore == 7 }, true)
    }

    @Test
    fun `updateBirthday can remove all reminders`() = runTest {
        // Arrange: Birthday 1 has reminders
        val originalData = repo.observeSingleBirthdayWithReminders(1).first()
        assertThat(originalData?.reminders?.size).isEqualTo(2)

        // Act: Update with empty reminders list
        val result = usecase(
            Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993),
            emptyList()  // Remove all reminders
        )

        // Assert: Reminders are gone
        assertTrue(result.isSuccess)
        val updatedData = repo.observeSingleBirthdayWithReminders(1).first()
        assertThat(updatedData?.reminders).equals(emptyList<Reminder>())
    }

    @Test
    fun `updateBirthday only affects the specified birthday`() = runTest {
        // Act: Update birthday 1
        usecase(
            Birthday(id = 1, name = "Lina Updated", day = 12, month = 4, year = 1993),
            emptyList()
        )

        // Assert: Birthday 2 is unchanged
        val birthday2 = repo.observeSingleBirthday(2).first()
        assertThat(birthday2?.name).isEqualTo("Lauren")  // Still the original name
    }

}