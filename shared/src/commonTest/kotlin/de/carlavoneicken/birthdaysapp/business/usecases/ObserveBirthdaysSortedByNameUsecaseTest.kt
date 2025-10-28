package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.collections.listOf
import kotlin.test.Test
import kotlin.test.assertTrue

class ObserveBirthdaysSortedByNameUsecaseTest {

    @Test
    fun `returns alphabetically sorted list of birthdays`() = runTest {
        val fakeRepo = FakeBirthdaysRepository(
            listOf(
                Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993),
                Birthday(id = 2, name = "Lauren", day = 31, month = 12, year = 1992),
                Birthday(id = 3, name = "Stine", day = 22, month = 2, year = 1993),
                Birthday(id = 4, name = "Lene", day = 18, month = 8, year = 1992)
            )
        )

        val usecase = ObserveBirthdaysSortedByNameUsecase(fakeRepo)

        val result: List<Birthday> = usecase().first()

        assertThat(listOf("Lauren", "Lene", "Lina", "Stine")).equals(result.map { it.name })
    }

    @Test
    fun `returns empty list when there are no saved birthdays`() = runTest {
        val fakeRepo = FakeBirthdaysRepository(listOf())

        val usecase = ObserveBirthdaysSortedByNameUsecase(fakeRepo)

        val result: List<Birthday> = usecase().first()

        assertTrue(result.isEmpty())
    }
}