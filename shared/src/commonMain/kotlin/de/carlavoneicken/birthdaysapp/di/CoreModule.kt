package de.carlavoneicken.birthdaysapp.di

import de.carlavoneicken.birthdaysapp.business.usecases.CreateBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.DeleteBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByNameUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByUpcomingUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.UpdateBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdayDetailViewModel
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdaysViewModel
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.data.database.BirthdayDao
import de.carlavoneicken.birthdaysapp.data.database.BirthdaysDatabase
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepositoryImpl
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysTestRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun coreModule(useFakeData: Boolean = false): Module = module {
    single<BirthdayDao> { get<BirthdaysDatabase>().getBirthdayDao() }

    // useFakeData for testing purposes
    if (useFakeData) {
        single<BirthdaysRepository> {
            BirthdaysTestRepository()
        }
    } else {
        single<BirthdaysRepository> {
            BirthdaysRepositoryImpl(get())
        }
    }

    single<CreateBirthdayUsecase> {
        CreateBirthdayUsecase(get())
    }
    single<DeleteBirthdayUsecase> {
        DeleteBirthdayUsecase(get())
    }
    single<ObserveBirthdaysSortedByNameUsecase> {
        ObserveBirthdaysSortedByNameUsecase(get())
    }
    single<ObserveBirthdaysSortedByUpcomingUsecase> {
        ObserveBirthdaysSortedByUpcomingUsecase(get())
    }
    single<ObserveSingleBirthdayUsecase> {
        ObserveSingleBirthdayUsecase(get())
    }
    single<UpdateBirthdayUsecase> {
        UpdateBirthdayUsecase(get())
    }

    viewModel { BirthdaysViewModel() }
    viewModel { (birthdayId: Long?) ->
        EditBirthdayViewModel(birthdayId)
    }
    viewModel { (birthdayId: Long) ->
        BirthdayDetailViewModel(birthdayId)
    }
}