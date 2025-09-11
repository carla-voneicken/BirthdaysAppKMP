package de.carlavoneicken.birthdaysapp.business.usecases

import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.repositories.BirthdaysRepository

class DeleteBirthdayUsecase(private val repo: BirthdaysRepository) {
    suspend operator fun invoke(birthday: Birthday): Result<Unit> {
        return repo.deleteBirthday(birthday)
    }
}