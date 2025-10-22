package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository

class DeleteBirthdayUsecase(private val repo: BirthdaysRepository) {
    @NativeCoroutines
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repo.deleteBirthdayById(id)
    }
}