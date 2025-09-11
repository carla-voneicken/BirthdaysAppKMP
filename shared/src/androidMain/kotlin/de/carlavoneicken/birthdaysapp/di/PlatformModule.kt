package de.carlavoneicken.birthdaysapp.di

import de.carlavoneicken.birthdaysapp.data.database.BirthdaysDatabase
import de.carlavoneicken.birthdaysapp.data.database.getAppDatabase
import de.carlavoneicken.birthdaysapp.data.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<BirthdaysDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getAppDatabase(builder)
    }
}