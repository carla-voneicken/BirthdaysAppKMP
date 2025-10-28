package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class DeleteBirthdayUsecaseTest {
    @Test
    fun `deleteBirthday returns success`() = runTest {
        val fakeRepo = FakeBirthdaysRepository(
            listOf(
                Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993),
                Birthday(id = 2, name = "Lauren", day = 31, month = 12, year = 1992),
                Birthday(id = 3, name = "Stine", day = 22, month = 2, year = 1993),
                Birthday(id = 4, name = "Lene", day = 18, month = 8, year = 1992)
            )
        )

        val usecase = DeleteBirthdayUsecase(fakeRepo)

        val result = usecase(2)

        assertTrue(result.isSuccess)
    }


    @Test
    fun `deleteBirthday returns failure when repo throws`() = runTest {
        val fakeRepo = FakeBirthdaysRepository()
        fakeRepo.shouldThrowOnCreate = true   // simulate DB exception
        val usecase = DeleteBirthdayUsecase(fakeRepo)

        val result = usecase(2)

        assertTrue(result.isFailure)
        assertThat("Simulated database error").equals(result.exceptionOrNull()?.message)
    }
}