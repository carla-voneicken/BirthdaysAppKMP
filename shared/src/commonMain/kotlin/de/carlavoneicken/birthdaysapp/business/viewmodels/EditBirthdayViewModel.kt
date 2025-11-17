package de.carlavoneicken.birthdaysapp.business.viewmodels

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import de.carlavoneicken.birthdaysapp.business.usecases.CreateBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.UpdateBirthdayUsecase
import de.carlavoneicken.birthdaysapp.data.models.BirthdayInput
import de.carlavoneicken.birthdaysapp.data.utils.BirthdayValidationResult
import de.carlavoneicken.birthdaysapp.data.utils.validateBirthdayInput
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditBirthdayViewModel(birthdayId: Long?): ViewModel(), KoinComponent {

    private val createBirthdayUsecase: CreateBirthdayUsecase by inject()
    private val updateBirthdayUsecase: UpdateBirthdayUsecase by inject()
    private val observeSingleBirthdayUsecase: ObserveSingleBirthdayUsecase by inject()

    data class UiState(
        val id: Long? = null,
        val name: String = "",
        val day: String = "",
        val month: String = "",
        val year: String = "",
        val errorMessage: String? = null,
        val successMessage: String? = null
    ) {
        // computed property isNew -> true if the id is null
        val isNew: Boolean get() = id == null
    }

    private val _uiState = MutableStateFlow(viewModelScope, UiState(id = birthdayId))
    @NativeCoroutinesState
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // if the birthdayId is not null (aka if user wants to edit an existing birthday), load that data
        if (birthdayId != null) {
            loadBirthday(birthdayId)
        }
    }

    // load birthday data only once from the database
    private var hasLoadedInitialData = false
    private fun loadBirthday(id: Long) {
        viewModelScope.launch {
            observeSingleBirthdayUsecase(id).firstOrNull()?.let { birthday ->
                if (!hasLoadedInitialData) {
                    updateState {
                        copy(
                            id = birthday.id,
                            name = birthday.name,
                            day = birthday.day.toString(),
                            month = birthday.month.toString(),
                            year = birthday.year?.toString() ?: ""
                        )
                    }
                    hasLoadedInitialData = true
                }
            }
        }
    }

    private fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }

    // Functions to update individual fields
    fun updateName(name: String) {
        updateState { copy(name = name) }
    }

    fun updateDay(day: String) {
        updateState { copy(day = day) }
    }

    fun updateMonth(month: String) {
        updateState { copy(month = month) }
    }

    fun updateYear(year: String) {
        updateState { copy(year = year) }
    }


    fun saveBirthday() {
        val currentState = _uiState.value
        // values the user entered in the text fields
        val birthdayInput = BirthdayInput(
            name = currentState.name,
            day = currentState.day,
            month = currentState.month,
            year = currentState.year.ifBlank { null }
        )

        // validate input (e.g. if it represents a valid date)
        when (val validationResult = validateBirthdayInput(birthdayInput)) {
            // if validation was successful, save the new or updated birthday to the database
            is BirthdayValidationResult.Success -> {
                // convert birthday from a BirthdayInput object to a Birthday object that can be saved
                var birthday = birthdayInput.toDomain()
                // If editing (aka the current id is not null), preserve the id
                if (currentState.id != null) {
                    birthday = birthday.copy(id = currentState.id)
                }
                viewModelScope.launch {
                    if (currentState.isNew) {
                        // New birthday
                        createBirthdayUsecase(birthday)
                            .onSuccess { updateState { copy(successMessage = "Birthday saved.") } }
                            .onFailure { e -> updateState { copy(errorMessage = "Error: ${e.message}") } }
                    } else {
                        // Existing birthday
                        updateBirthdayUsecase(birthday)
                            .onSuccess { updateState { copy(successMessage = "Birthday updated.") } }
                            .onFailure { e -> updateState { copy(errorMessage = "Error: ${e.message}") } }
                    }
                }
            }
            // if the validation returns an error, save it to uiState and to display it
            is BirthdayValidationResult.Error -> {
                updateState { copy(errorMessage = validationResult.message) }
            }
        }
    }

    fun clearMessages() {
        updateState { copy(successMessage = null, errorMessage = null) }
    }
}