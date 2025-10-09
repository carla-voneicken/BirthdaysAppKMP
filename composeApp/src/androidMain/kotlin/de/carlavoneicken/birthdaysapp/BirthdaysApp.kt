package de.carlavoneicken.birthdaysapp

import android.app.Application
import de.carlavoneicken.birthdaysapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class BirthdaysApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            appDeclaration = { androidContext(this@BirthdaysApp) },
            useFakeData = true
        )
    }
}