package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import de.carlavoneicken.birthdaysapp.di.BaseKoinTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.getValue
import kotlin.test.Test
import kotlin.test.assertTrue

class ObserveBirthdaysSortedByNameUsecaseTest: BaseKoinTest() {
    private val repo: BirthdaysRepository by inject<BirthdaysRepository>()
    private val usecase = ObserveBirthdaysSortedByNameUsecase()

    @Test
    fun `returns alphabetically sorted list of birthdays`() = runTest {
        val result: List<Birthday> = usecase().first()

        assertThat(listOf("Lauren", "Lene", "Lina", "Stine")).equals(result.map { it.name })
    }

    @Test
    fun `returns empty list when there are no saved birthdays`() = runTest {
        // casting the repository so the test knows it's actually a FakeBirthdaysRepository
        // (the others don't have the clear() function)
        val fakeRepo = repo as FakeBirthdaysRepository
        fakeRepo.clear()

        val result: List<Birthday> = usecase().first()

        assertTrue(result.isEmpty())
    }
}