package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ObserveBirthdaysSortedByUpcomingUsecase(): KoinComponent {

    private val repo: BirthdaysRepository by inject()

    @NativeCoroutines
    operator fun invoke(): Flow<List<Birthday>> {
        return repo.observeBirthdaysSortedByUpcoming()
    }
}