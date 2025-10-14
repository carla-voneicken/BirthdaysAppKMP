package de.carlavoneicken.birthdaysapp.utils

import de.carlavoneicken.birthdaysapp.R
import androidx.annotation.DrawableRes
import de.carlavoneicken.birthdaysapp.data.models.ZodiacSign

@DrawableRes
fun ZodiacSign.toDrawableRes(): Int = when (this) {
    ZodiacSign.CAPRICORN -> R.drawable.capricorn_
    ZodiacSign.AQUARIUS -> R.drawable.aquarius_
    ZodiacSign.PISCES -> R.drawable.pisces_
    ZodiacSign.ARIES -> R.drawable.aries_
    ZodiacSign.TAURUS -> R.drawable.taurus_
    ZodiacSign.GEMINI -> R.drawable.gemini_
    ZodiacSign.CANCER -> R.drawable.cancer_
    ZodiacSign.LEO -> R.drawable.leo_
    ZodiacSign.VIRGO -> R.drawable.virgo_
    ZodiacSign.LIBRA -> R.drawable.libra_
    ZodiacSign.SCORPIO -> R.drawable.scorpio_
    ZodiacSign.SAGITTARIUS -> R.drawable.sagittarius_
}