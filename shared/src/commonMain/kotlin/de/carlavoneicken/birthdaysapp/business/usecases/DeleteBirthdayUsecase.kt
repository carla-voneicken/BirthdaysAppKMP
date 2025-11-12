package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteBirthdayUsecase(): KoinComponent {

    private val repo: BirthdaysRepository by inject()

    @NativeCoroutines
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repo.deleteBirthdayById(id)
    }
}