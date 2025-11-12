package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.di.BaseKoinTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ObserveSingleBirthdayUsecaseTest: BaseKoinTest() {
    private val usecase = ObserveSingleBirthdayUsecase()

    @Test
    fun `returns correct birthday when observed`() = runTest {
        val result = usecase(1).first()

        assertThat("Lina").equals(result?.name)
    }

    @Test
    fun `returns null for unknown id`() = runTest {
        val result = usecase(42).first()

        assertThat(null).equals(result)
    }
}