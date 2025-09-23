package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import kotlinx.coroutines.flow.Flow

class ObserveSingleBirthdayUsecase(private val repo: BirthdaysRepository) {
    @NativeCoroutines
    suspend operator fun invoke(id: Long): Flow<Birthday?> {
        return repo.observeSingleBirthday(id)
    }
}