package de.carlavoneicken.birthdaysapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    useFakeData: Boolean = false
) {
    startKoin {
        appDeclaration()
        modules(coreModule(useFakeData), platformModule())
    }
}

fun initKoinIos(useFakeData: Boolean = false) = initKoin(appDeclaration = {}, useFakeData)
