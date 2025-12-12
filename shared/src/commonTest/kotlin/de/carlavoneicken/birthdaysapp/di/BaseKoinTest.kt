package de.carlavoneicken.birthdaysapp.di

import de.carlavoneicken.birthdaysapp.business.usecases.CreateBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.DeleteBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByNameUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByUpcomingUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.UpdateBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.FakeBirthdaysRepository
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


abstract class BaseKoinTest: KoinTest {

    // setup Koin for the tests
    @BeforeTest
    fun setUpKoin() {
        startKoin { modules(testModule()) }
    }

    // break Koin down after the test ran
    @AfterTest
    fun tearDownKoin() {
        stopKoin()
    }

    open fun testModule() = module {
        single<BirthdaysRepository> { FakeBirthdaysRepository() }

        single<CreateBirthdayWithRemindersUsecase> {
            CreateBirthdayWithRemindersUsecase()
        }
        single<DeleteBirthdayUsecase> {
            DeleteBirthdayUsecase()
        }
        single<ObserveBirthdaysSortedByNameUsecase> {
            ObserveBirthdaysSortedByNameUsecase()
        }
        single<ObserveBirthdaysSortedByUpcomingUsecase> {
            ObserveBirthdaysSortedByUpcomingUsecase()
        }
        single<ObserveSingleBirthdayUsecase> {
            ObserveSingleBirthdayUsecase()
        }
        single<UpdateBirthdayWithRemindersUsecase> {
            UpdateBirthdayWithRemindersUsecase()
        }
    }
}