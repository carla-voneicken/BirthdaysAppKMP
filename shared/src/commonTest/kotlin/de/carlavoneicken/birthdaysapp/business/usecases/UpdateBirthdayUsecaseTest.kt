package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import de.carlavoneicken.birthdaysapp.di.BaseKoinTest
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class UpdateBirthdayUsecaseTest: BaseKoinTest() {

    private val repo: BirthdaysRepository by inject<BirthdaysRepository>()
    private var usecase = UpdateBirthdayWithRemindersUsecase()

    @Test
    fun `updateBirthday returns success`() = runTest {
        val result = usecase(
            Birthday(0L, "Alice", 1, 3, 1990),
            emptyList()
        )

        assertTrue(result.isSuccess)
    }


    @Test
    fun `updateBirthday returns failure when repo throws`() = runTest {
        // casting the repository so the test knows it's actually a FakeBirthdaysRepository
        // (the others don't have the shouldThrowOnCreate variable)
        val fakeRepo = repo as FakeBirthdaysRepository
        fakeRepo.shouldThrowOnCreate = true   // simulate DB exception

        val result = usecase(
            Birthday(1, "Alice", 1, 3, 1990),
            emptyList()
        )

        assertTrue(result.isFailure)
        assertThat("Simulated database error").equals(result.exceptionOrNull()?.message)
    }
}