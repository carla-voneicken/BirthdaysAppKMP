package de.carlavoneicken.birthdaysapp.business.viewmodels

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import de.carlavoneicken.birthdaysapp.business.usecases.DeleteBirthdayUsecase
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BirthdayDetailViewModel(birthday: Birthday): ViewModel(), KoinComponent {
    private val deleteBirthdayUsecase: DeleteBirthdayUsecase by inject()

    data class UiState(
        val birthday: Birthday,
        val isDeleting: Boolean = true,
        val errorMessage: String? = null,
        val successMessage: String? = null
    )

    private val _uiState = MutableStateFlow(viewModelScope, UiState(birthday))
    @NativeCoroutinesState
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }

    fun deleteBirthday() {
        viewModelScope.launch {
            updateState { copy(isDeleting = true) }

            try {
                deleteBirthdayUsecase(uiState.value.birthday)
                updateState { copy(successMessage = "Geburtstag gelöscht", isDeleting = false) }
            } catch (e: Exception) {
                updateState { copy(errorMessage = "Löschen fehlgeschlagen: ${e.message}", isDeleting = false) }
            }
        }
    }
}