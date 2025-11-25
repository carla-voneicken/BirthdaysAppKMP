package de.carlavoneicken.birthdaysapp.di

import de.carlavoneicken.birthdaysapp.business.usecases.CreateBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.DeleteBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByNameUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByUpcomingUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.UpdateBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdayDetailViewModel
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdaysViewModel
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.data.database.BirthdayDao
import de.carlavoneicken.birthdaysapp.data.database.BirthdaysDatabase
import de.carlavoneicken.birthdaysapp.data.database.ReminderDao
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepositoryImpl
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysUITestRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun coreModule(useFakeData: Boolean = false): Module = module {
    single<BirthdayDao> { get<BirthdaysDatabase>().getBirthdayDao() }
    single<ReminderDao> { get<BirthdaysDatabase>().getReminderDao() }

    // useFakeData for testing purposes
    if (useFakeData) {
        single<BirthdaysRepository> {
            BirthdaysUITestRepository()
        }
    } else {
        single<BirthdaysRepository> {
            BirthdaysRepositoryImpl()
        }
    }

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
    single<ObserveSingleBirthdayWithRemindersUsecase> {
        ObserveSingleBirthdayWithRemindersUsecase()
    }
    single<UpdateBirthdayWithRemindersUsecase> {
        UpdateBirthdayWithRemindersUsecase()
    }

    viewModel { BirthdaysViewModel() }
    viewModel { (birthdayId: Long?) ->
        EditBirthdayViewModel(birthdayId)
    }
    viewModel { (birthdayId: Long) ->
        BirthdayDetailViewModel(birthdayId)
    }
}