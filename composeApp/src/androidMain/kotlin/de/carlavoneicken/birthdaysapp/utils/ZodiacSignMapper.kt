package de.carlavoneicken.birthdaysapp.utils

import de.carlavoneicken.birthdaysapp.R
import androidx.annotation.DrawableRes
import de.carlavoneicken.birthdaysapp.data.models.ZodiacSign

@DrawableRes
fun ZodiacSign.toDrawableRes(): Int = when (this) {
    ZodiacSign.CAPRICORN -> R.drawable.ic_capricorn_
    ZodiacSign.AQUARIUS -> R.drawable.ic_aquarius_
    ZodiacSign.PISCES -> R.drawable.ic_pisces_
    ZodiacSign.ARIES -> R.drawable.ic_aries_
    ZodiacSign.TAURUS -> R.drawable.ic_taurus_
    ZodiacSign.GEMINI -> R.drawable.ic_gemini_
    ZodiacSign.CANCER -> R.drawable.ic_cancer_
    ZodiacSign.LEO -> R.drawable.ic_leo_
    ZodiacSign.VIRGO -> R.drawable.ic_virgo_
    ZodiacSign.LIBRA -> R.drawable.ic_libra_
    ZodiacSign.SCORPIO -> R.drawable.ic_scorpio_
    ZodiacSign.SAGITTARIUS -> R.drawable.ic_sagittarius_
}