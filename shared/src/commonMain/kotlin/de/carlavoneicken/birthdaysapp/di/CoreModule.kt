package de.carlavoneicken.birthdaysapp.di

import de.carlavoneicken.birthdaysapp.data.database.BirthdayDao
import de.carlavoneicken.birthdaysapp.data.database.BirthdaysDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val coreModule: Module = module {
    single<BirthdayDao> { get<BirthdaysDatabase>().getBirthdayDao() }
}