package de.carlavoneicken.birthdaysapp.business.usecases

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository
import kotlinx.coroutines.flow.Flow

class ObserveBirthdaysSortedByNameUsecase(private val repo: BirthdaysRepository) {
    operator fun invoke(): Flow<List<Birthday>> {
        return repo.observeBirthdaysSortedByName()
    }
}