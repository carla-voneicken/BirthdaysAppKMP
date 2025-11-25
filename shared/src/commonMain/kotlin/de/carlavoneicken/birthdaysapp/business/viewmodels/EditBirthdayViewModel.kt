package de.carlavoneicken.birthdaysapp.business.viewmodels

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import de.carlavoneicken.birthdaysapp.business.usecases.CreateBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveSingleBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.UpdateBirthdayWithRemindersUsecase
import de.carlavoneicken.birthdaysapp.data.models.BirthdayInput
import de.carlavoneicken.birthdaysapp.data.models.toReminderUiState
import de.carlavoneicken.birthdaysapp.data.models.toRemindersDomain
import de.carlavoneicken.birthdaysapp.data.utils.BirthdayValidationResult
import de.carlavoneicken.birthdaysapp.data.utils.validateBirthdayInput
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditBirthdayViewModel(birthdayId: Long?): ViewModel(), KoinComponent {

    private val createBirthdayUsecase: CreateBirthdayWithRemindersUsecase by inject()
    private val updateBirthdayUsecase: UpdateBirthdayWithRemindersUsecase by inject()
    private val observeSingleBirthdayWithRemindersUsecase: ObserveSingleBirthdayWithRemindersUsecase by inject()

    data class UiState(
        val id: Long? = null,
        val name: String = "",
        val day: String = "",
        val month: String = "",
        val year: String = "",
        val reminders: ReminderUiState = ReminderUiState(),
        val errorMessage: String? = null,
        val successMessage: String? = null
    ) {
        // computed property isNew -> true if the id is null
        val isNew: Boolean get() = id == null
    }

    data class ReminderUiState(
        val remindMe: Boolean = false,
        val onTheDay: Boolean = false,
        val dayBefore: Boolean = false,
        val sevenDaysBefore: Boolean = false,
        val customEnabled: Boolean = false,
        val customDays: String = ""
    )

    enum class ReminderOption {
        ON_THE_DAY,
        DAY_BEFORE,
        SEVEN_DAYS_BEFORE,
        CUSTOM_ENABLED
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
            observeSingleBirthdayWithRemindersUsecase(id).firstOrNull()?.let { bwr ->
                if (!hasLoadedInitialData) {
                    val birthday = bwr.birthday
                    val reminders = bwr.reminders

                    updateState {
                        copy(
                            id = birthday.id,
                            name = birthday.name,
                            day = birthday.day.toString(),
                            month = birthday.month.toString(),
                            year = birthday.year?.toString() ?: "",
                            reminders = reminders.toReminderUiState()
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

    fun setRemindMe(remindMe: Boolean) {
        _uiState.update { state ->
            state.copy(
                reminders = state.reminders.copy(remindMe = remindMe)
            )
        }
    }

    fun toggleReminderOption(option: ReminderOption, checked: Boolean) {
        _uiState.update { state ->
            val r = state.reminders
            val updatedReminders = when (option) {
                ReminderOption.ON_THE_DAY ->
                    r.copy(onTheDay = checked)

                ReminderOption.DAY_BEFORE ->
                    r.copy(dayBefore = checked)

                ReminderOption.SEVEN_DAYS_BEFORE ->
                    r.copy(sevenDaysBefore = checked)

                ReminderOption.CUSTOM_ENABLED ->
                    r.copy(customEnabled = checked)
            }
            state.copy(reminders = updatedReminders)
        }
    }

    fun setCustomDays(days: String) {
        _uiState.update { state ->
            state.copy(
                reminders = state.reminders.copy(customDays = days)
            )
        }
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

                val reminders = currentState.reminders.toRemindersDomain(
                    birthdayId = birthday.id.takeIf { !currentState.isNew }
                )

                viewModelScope.launch {
                    if (currentState.isNew) {
                        // New birthday
                        createBirthdayUsecase(birthday, reminders)
                            .onSuccess { updateState { copy(successMessage = "Birthday saved.") } }
                            .onFailure { e -> updateState { copy(errorMessage = "Error: ${e.message}") } }
                    } else {
                        // Existing birthday
                        updateBirthdayUsecase(birthday, reminders)
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