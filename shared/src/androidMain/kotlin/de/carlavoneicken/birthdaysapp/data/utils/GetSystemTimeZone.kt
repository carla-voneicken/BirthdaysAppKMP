package de.carlavoneicken.birthdaysapp.data.utils

import kotlinx.datetime.TimeZone

actual fun getSystemTimeZone(): TimeZone = TimeZone.currentSystemDefault()
