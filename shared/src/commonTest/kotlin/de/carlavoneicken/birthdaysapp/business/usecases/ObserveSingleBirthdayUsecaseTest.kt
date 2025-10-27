package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ObserveSingleBirthdayUsecaseTest {

    @Test
    fun `returns correct birthday when observed`() = runTest {
        val fakeRepo = FakeBirthdaysRepository(
            listOf(Birthday(id = 1, name = "Lina", day = 12, month = 4, year = 1993))
        )

        val usecase = ObserveSingleBirthdayUsecase(fakeRepo)

        val result = usecase(1).first()

        assertThat("Lina").equals(result?.name)
    }

    @Test
    fun `returns null for unknown id`() = runTest {
        val fakeRepo = FakeBirthdaysRepository()
        val usecase = ObserveSingleBirthdayUsecase(fakeRepo)

        val result = usecase(42).first()

        assertThat(null).equals(result)
    }
}