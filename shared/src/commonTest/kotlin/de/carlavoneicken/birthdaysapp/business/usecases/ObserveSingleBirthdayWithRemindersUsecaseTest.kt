package de.carlavoneicken.birthdaysapp.business.usecases

import assertk.assertThat
import de.carlavoneicken.birthdaysapp.di.BaseKoinTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first

import kotlin.test.Test
import kotlin.test.assertNotNull

class ObserveSingleBirthdayWithRemindersUsecaseTest: BaseKoinTest() {

    private val usecase = ObserveSingleBirthdayWithRemindersUsecase()

    @Test
    fun `returns correct birthday with reminders when observed`() = runTest {
        val result = usecase(1).first()

        assertNotNull(result)
        assertThat("Lina").equals(result.birthday.name)

        assertThat(2).equals(result.reminders.size) // there are two reminders in the fake data for Lina
    }

    @Test
    fun `returns null for unknown id`() = runTest {
        val result = usecase(42).first()

        assertThat(null).equals(result)
    }
}