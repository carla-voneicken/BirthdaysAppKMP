package de.carlavoneicken.birthdaysapp.business.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import de.carlavoneicken.birthdaysapp.business.usecases.DeleteBirthdayUsecase
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _uiState = MutableStateFlow(UiState(birthday))
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