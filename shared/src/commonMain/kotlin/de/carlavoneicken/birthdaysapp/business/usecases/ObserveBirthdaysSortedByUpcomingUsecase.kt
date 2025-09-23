package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import kotlinx.coroutines.flow.Flow

class ObserveBirthdaysSortedByUpcomingUsecase(private val repo: BirthdaysRepository) {
    @NativeCoroutines
    operator fun invoke(): Flow<List<Birthday>> {
        return repo.observeBirthdaysSortedByUpcoming()
    }
}