package de.carlavoneicken.birthdaysapp.business.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByNameUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByUpcomingUsecase
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalCoroutinesApi::class)
class BirthdaysViewModel(): ViewModel(), KoinComponent {
    private val observeBirthdaysSortedByNameUsecase: ObserveBirthdaysSortedByNameUsecase by inject()
    private val observeBirthdaysSortedByUpcomingUsecase: ObserveBirthdaysSortedByUpcomingUsecase by inject()

    enum class SortMode { BY_NAME, BY_UPCOMING }

    data class UiState(
        val birthdays: List<Birthday> = emptyList(),
        val isLoading: Boolean = true,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    @NativeCoroutinesState
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sortMode = MutableStateFlow(SortMode.BY_UPCOMING)
    @NativeCoroutinesState
    val sortMode: StateFlow<SortMode> = _sortMode.asStateFlow()

    init {
        viewModelScope.launch {
            _sortMode
                .flatMapLatest { mode ->
                    when (mode) {
                        SortMode.BY_NAME -> observeBirthdaysSortedByNameUsecase()
                        SortMode.BY_UPCOMING -> observeBirthdaysSortedByUpcomingUsecase()
                    }
                }
                .catch { e: Throwable ->
                    updateState { copy(errorMessage = e.message ?: "Unbekannter Fehler") }
                }
                .collect { birthdays ->
                updateState { copy(birthdays = birthdays, isLoading = false, errorMessage = null)}
            }
        }
    }

    private fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }


}