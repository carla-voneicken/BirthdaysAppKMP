package de.carlavoneicken.birthdaysapp.business.usecases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.Reminder
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateBirthdayWithRemindersUsecase(): KoinComponent {

    private val repo: BirthdaysRepository by inject()

    @NativeCoroutines
    suspend operator fun invoke(birthday: Birthday, reminders: List<Reminder>): Result<Unit> {
        return repo.updateBirthdayWithReminders(birthday, reminders)
    }
}