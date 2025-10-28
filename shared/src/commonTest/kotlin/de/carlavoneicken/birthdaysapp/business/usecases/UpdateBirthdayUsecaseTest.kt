package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class UpdateBirthdayUsecaseTest {

    private lateinit var fakeRepo: FakeBirthdaysRepository
    private lateinit var usecase: UpdateBirthdayUsecase

    @BeforeTest
    fun setUp() {
        fakeRepo = FakeBirthdaysRepository(
            listOf(
                Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993),
                Birthday(id = 2, name = "Lauren", day = 31, month = 12, year = 1992),
                Birthday(id = 3, name = "Stine", day = 22, month = 2, year = 1993),
                Birthday(id = 4, name = "Lene", day = 18, month = 8, year = 1992)
            )
        )
        usecase = UpdateBirthdayUsecase(fakeRepo)
    }

    @Test
    fun `updateBirthday returns success`() = runTest {
        val result = usecase(Birthday(0L, "Alice", 1, 3, 1990))

        assertTrue(result.isSuccess)
    }


    @Test
    fun `updateBirthday returns failure when repo throws`() = runTest {
        fakeRepo.shouldThrowOnCreate = true   // simulate DB exception

        val result = usecase(Birthday(1, "Alice", 1, 3, 1990))

        assertTrue(result.isFailure)
        assertThat("Simulated database error").equals(result.exceptionOrNull()?.message)
    }
}