package de.carlavoneicken.birthdaysapp.utils

import androidx.compose.ui.graphics.Color
import de.carlavoneicken.birthdaysapp.ui.SharedPalette

val GoldPrimary = SharedPalette.GoldPrimary.toComposeColor()
val TurquoiseSecondary = SharedPalette.TurquoiseSecondary.toComposeColor()
val OrangeAccent = SharedPalette.OrangeAccent.toComposeColor()

val TextPrimary = SharedPalette.TextPrimary.toComposeColor()
val TextSecondary = SharedPalette.TextSecondary.toComposeColor()
val BackgroundLight = SharedPalette.BackgroundLight.toComposeColor()
val BackgroundDark = SharedPalette.BackgroundDark.toComposeColor()

fun Long.toComposeColor(): Color {
    val a = ((this ushr 24) and 0xFF).toInt()
    val r = ((this ushr 16) and 0xFF).toInt()
    val g = ((this ushr 8)  and 0xFF).toInt()
    val b = ( this          and 0xFF).toInt()
    return Color(r, g, b, a)
}