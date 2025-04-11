package com.wngud.pomosound.ui.presentation.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.pomosound.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimerUiState(
    val isLoading: Boolean = true,
    val bgResource: Int = 0,
    val errorMessage: String? = null
)

sealed class TimerEvent {
    data class SoundItemClicked(val id: Int) : TimerEvent()
}

sealed class TimerSideEffect {
    object NavigateBack : TimerSideEffect()
}

@HiltViewModel
class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placeRepository: PlaceRepository
) : ViewModel() {
    private val placeId: Int = savedStateHandle.get<Int>(PLACE_ID_SAVED_STATE_KEY)!!

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<TimerSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getPlace()
    }

    private fun getPlace() {
        viewModelScope.launch {
            placeRepository.getPlaceById(placeId).collectLatest {
                _uiState.value = TimerUiState(
                    isLoading = false,
                    bgResource = it.videoResId
                )
            }
        }
    }

    companion object {
        private const val PLACE_ID_SAVED_STATE_KEY = "placeId"
    }
}