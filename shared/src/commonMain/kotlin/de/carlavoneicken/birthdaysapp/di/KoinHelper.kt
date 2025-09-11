package de.carlavoneicken.birthdaysapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(coreModule, platformModule())
    }
}

fun initKoinIos() = initKoin(appDeclaration = {})