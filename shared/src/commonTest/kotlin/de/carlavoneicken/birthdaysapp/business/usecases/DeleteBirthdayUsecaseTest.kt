package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import de.carlavoneicken.birthdaysapp.di.BaseKoinTest
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class DeleteBirthdayUsecaseTest: BaseKoinTest() {
    private val repo: BirthdaysRepository by inject<BirthdaysRepository>()
    private val usecase = DeleteBirthdayUsecase()


    @Test
    fun `deleteBirthday returns success`() = runTest {
        val result = usecase(2)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteBirthday returns failure when repo throws`() = runTest {
        // casting the repository so the test knows it's actually a FakeBirthdaysRepository
        // (the others don't have the shouldThrowOnCreate variable)
        val fakeRepo = repo as FakeBirthdaysRepository
        fakeRepo.shouldThrowOnCreate = true   // simulate DB exception

        val result = usecase(2)

        assertTrue(result.isFailure)
        assertThat("Simulated database error").equals(result.exceptionOrNull()?.message)
    }
}