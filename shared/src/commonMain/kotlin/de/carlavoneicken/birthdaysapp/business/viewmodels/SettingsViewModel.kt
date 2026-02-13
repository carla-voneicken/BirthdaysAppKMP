package de.carlavoneicken.birthdaysapp.business.viewmodels

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

class SettingsViewModel(): ViewModel(), KoinComponent {

    data class SettingsUiState(
        val reminderTime: String = "9:00 AM",
        val theme: Theme = Theme.SYSTEM,
        val showTimePickerDialog: Boolean = false,
        val showThemeDialog: Boolean = false,
        val showImportDialog: Boolean = false,
        val showExportDialog: Boolean = false
    )

    enum class Theme(val displayName: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SYSTEM("System default")
    }

    private val _uiState = MutableStateFlow(viewModelScope, SettingsUiState())
    @NativeCoroutinesState
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun showTimePickerDialog() {
        _uiState.update { it.copy(showTimePickerDialog = true) }
    }

    fun showThemeDialog() {

    }

    fun showImportOptions() {

    }

    fun showExportOptions() {

    }

    fun hideTimePickerDialog() {
        _uiState.update { it.copy(showTimePickerDialog = false) }
    }

    fun updateReminderTime(hour: Int, minute: Int) {
        // format time to a string with "HH:MM" format
        val hourStr = hour.toString().padStart(2, '0')
        val minuteStr = minute.toString().padStart(2, '0')
        val timeString = "$hourStr:$minuteStr"

        _uiState.update { it.copy(reminderTime = timeString)}
    }

    fun hideThemeDialog() {

    }

    fun updateTheme(theme: Theme) {

    }
}