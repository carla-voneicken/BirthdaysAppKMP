package de.carlavoneicken.birthdaysapp.business.viewmodels

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByNameUsecase
import de.carlavoneicken.birthdaysapp.business.usecases.ObserveBirthdaysSortedByUpcomingUsecase
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BirthdaysViewModel(): ViewModel(), KoinComponent {
    private val observeBirthdaysSortedByNameUsecase: ObserveBirthdaysSortedByNameUsecase by inject()
    private val observeBirthdaysSortedByUpcomingUsecase: ObserveBirthdaysSortedByUpcomingUsecase by inject()

    enum class SortMode { BY_NAME, BY_UPCOMING }

    data class UiState(
        val birthdays: List<Birthday> = emptyList(),
        val isLoading: Boolean = true,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(viewModelScope, UiState())
    @NativeCoroutinesState
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sortMode = MutableStateFlow(viewModelScope, SortMode.BY_UPCOMING)
    @NativeCoroutinesState
    val sortMode: StateFlow<SortMode> = _sortMode.asStateFlow()


    // flatMapLatest means: start collection from the flow returned for the latest request -> cancel any previous
    // flow that was running and always keep only the LATEST one active
    // So when when the _sortMode is changed, flatMapLatest cancels the previous flow and starts a new one
    @OptIn(ExperimentalCoroutinesApi::class)
    private val birthdaysFlow: Flow<List<Birthday>> = _sortMode
        .flatMapLatest { mode ->
            when (mode) {
                SortMode.BY_NAME -> observeBirthdaysSortedByNameUsecase()
                SortMode.BY_UPCOMING -> observeBirthdaysSortedByUpcomingUsecase()
            }
        }

    // on initialization we're launching the birthdaysFlow, catching any errors and collecting the
    // birthdays data to update _the uiState
    init {
        viewModelScope.launch {
            birthdaysFlow
                .catch { e: Throwable ->
                    updateState { copy(errorMessage = e.message ?: "Unbekannter Fehler") }
                }
                .collect { birthdays ->
                    updateState { copy(birthdays = birthdays, isLoading = false, errorMessage = null) }
                }
        }
    }

    private fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }

    // change sortMode to the mode provided as a parameter
    fun setSortMode(mode: SortMode) {
        _sortMode.value = mode
    }
}