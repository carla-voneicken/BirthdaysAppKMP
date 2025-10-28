package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CreateBirthdayUsecaseTest {
    @Test
    fun `createBirthday returns success`() = runTest {
        val fakeRepo = FakeBirthdaysRepository()

        val usecase = CreateBirthdayUsecase(fakeRepo)

        val result = usecase(Birthday(0L,"Alice", 1, 3, 1990))

        assertTrue(result.isSuccess)
    }


    @Test
    fun `createBirthday returns failure when repo throws`() = runTest {
        val fakeRepo = FakeBirthdaysRepository()
        fakeRepo.shouldThrowOnCreate = true   // simulate DB exception
        val usecase = CreateBirthdayUsecase(fakeRepo)

        val result = usecase(Birthday(1, "Alice", 1, 3, 1990))

        assertTrue(result.isFailure)
        assertThat("Simulated database error").equals(result.exceptionOrNull()?.message)
    }
}