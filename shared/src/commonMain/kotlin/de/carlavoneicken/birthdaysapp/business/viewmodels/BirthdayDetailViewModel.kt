package de.carlavoneicken.birthdaysapp.business.viewmodels

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import de.carlavoneicken.birthdaysapp.business.usecases.DeleteBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayUsecase
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BirthdayDetailViewModel(
    private val birthdayId: Long
): ViewModel(), KoinComponent {
    private val observeSingleBirthdayUsecase: ObserveSingleBirthdayUsecase by inject()
    private val deleteBirthdayUsecase: DeleteBirthdayUsecase by inject()

    data class UiState(
        val birthday: Birthday? = null,
        val isLoading: Boolean = true,
        val isDeleting: Boolean = false,
        val errorMessage: String? = null,
        val successMessage: String? = null
    )

    private val _uiState = MutableStateFlow(viewModelScope, UiState())
    @NativeCoroutinesState
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            observeSingleBirthdayUsecase(birthdayId)
                // onStart is a Flow operator that runs BEFORE the first value is emitted
                // -> emit(null) sends an initial value into the flow before the database emits anything
                .onStart { emit(null) }
                .collect { birthday ->
                    _uiState.update {
                        it.copy(
                            birthday = birthday,
                            isLoading = false,
                            errorMessage = if (birthday == null) "Not found" else null
                        )
                    }
                }
        }
    }

    private fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }

    fun deleteBirthday() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            val result = deleteBirthdayUsecase(birthdayId)
            _uiState.update {
                if (result.isSuccess) it.copy(successMessage = "Deleted birthday", isDeleting = false)
                else it.copy(errorMessage = result.exceptionOrNull()?.message ?: "Deletion failed", isDeleting = false)
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}