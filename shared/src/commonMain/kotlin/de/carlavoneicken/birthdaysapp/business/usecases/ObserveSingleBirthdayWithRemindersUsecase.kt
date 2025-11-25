package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.BirthdayWithReminders
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ObserveSingleBirthdayWithRemindersUsecase(): KoinComponent {
    private val repo: BirthdaysRepository by inject()

    @NativeCoroutines
    suspend operator fun invoke(id: Long): Flow<BirthdayWithReminders?> {
        return repo.observeSingleBirthdayWithReminders(id)
    }
}