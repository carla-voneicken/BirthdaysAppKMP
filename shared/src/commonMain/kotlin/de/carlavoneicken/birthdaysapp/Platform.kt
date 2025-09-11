package de.carlavoneicken.birthdaysapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform