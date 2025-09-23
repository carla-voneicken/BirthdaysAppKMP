package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository

class CreateBirthdayUsecase(private val repo: BirthdaysRepository) {
    @NativeCoroutines
    suspend operator fun invoke(birthday: Birthday): Result<Unit> {
        return repo.createBirthday(birthday)
    }
}