package de.carlavoneicken.birthdaysapp.di

import de.carlavoneicken.birthdaysapp.data.database.BirthdaysDatabase
import de.carlavoneicken.birthdaysapp.data.database.getAppDatabase
import de.carlavoneicken.birthdaysapp.data.database.getDatabaseBuilder
import de.carlavoneicken.birthdaysapp.notifications.ReminderSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<BirthdaysDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getAppDatabase(builder)
    }

    single<ReminderSettings> { ReminderSettings(androidContext()) }
}