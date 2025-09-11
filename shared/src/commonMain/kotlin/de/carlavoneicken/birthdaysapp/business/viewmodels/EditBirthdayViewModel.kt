package de.carlavoneicken.birthdaysapp.business.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import de.carlavoneicken.birthdaysapp.business.usecases.CreateBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.UpdateBirthdayUsecase
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.models.BirthdayInput
import de.carlavoneicken.birthdaysapp.data.utils.BirthdayValidationResult
import de.carlavoneicken.birthdaysapp.data.utils.validateBirthdayInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditBirthdayViewModel(birthday: Birthday?): ViewModel(), KoinComponent {

    private val createBirthdayUsecase: CreateBirthdayUsecase by inject()
    private val updateBirthdayUsecase: UpdateBirthdayUsecase by inject()

    data class UiState(
        val id: Long? = null,
        val name: String = "",
        val day: String = "",
        val month: String = "",
        val year: String = "",
        val errorMessage: String? = null,
        val successMessage: String? = null
    ) {
        val isNew: Boolean get() = id == null
    }

    private val _uiState = MutableStateFlow(UiState(id = null))
    @NativeCoroutinesState
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }

    fun saveBirthday(uiState: UiState) {
        val birthdayInput = BirthdayInput(
            name = uiState.name,
            day = uiState.day,
            month = uiState.month,
            year = uiState.year
        )
        val validationResult = validateBirthdayInput(birthdayInput)

        when (validationResult) {
            is BirthdayValidationResult.Success -> {
                val birthday = birthdayInput.toDomain()
                viewModelScope.launch {
                    if (uiState.id == null) {
                        // New birthday
                        createBirthdayUsecase(birthday)
                            .onSuccess { updateState { copy(successMessage = "Geburtstag gespeichert!") } }
                            .onFailure { e -> updateState { copy(errorMessage = "Fehler: ${e.message}") } }
                    } else {
                        // Existing birthday
                        updateBirthdayUsecase(birthday)
                            .onSuccess { updateState { copy(successMessage = "Geburtstag aktualisiert!") } }
                            .onFailure { e -> updateState { copy(errorMessage = "Fehler: ${e.message}") } }
                    }
                }
            }
            is BirthdayValidationResult.Error -> {
                updateState { copy(errorMessage = validationResult.message) }
            }
        }
    }

    fun clearMessages() {
        updateState { copy(successMessage = null, errorMessage = null) }
    }
}