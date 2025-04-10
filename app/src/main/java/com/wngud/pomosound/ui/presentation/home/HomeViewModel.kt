package com.wngud.pomosound.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.pomosound.data.model.PlaceItemData
import com.wngud.pomosound.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val places: List<PlaceItemData> = emptyList(),
    val errorMessage: String? = null
)

sealed class HomeEvent {
    object LoadPlaces : HomeEvent()
}

sealed class HomeSideEffect {
    data class NavigateToSound(val id: Int) : HomeSideEffect()
    object NavigateToSetting : HomeSideEffect()
    data class ShowSnackbar(val message: String) : HomeSideEffect()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<HomeSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        postEvent(HomeEvent.LoadPlaces)
    }

    fun postEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadPlaces -> loadPlaces()
        }
    }

    private fun postSideEffect(sideEffect: HomeSideEffect) {
        viewModelScope.launch {
            _sideEffects.send(sideEffect)
        }
    }

    fun navigateToSound(id: Int) {
        postSideEffect(HomeSideEffect.NavigateToSound(id))
    }

    fun navigateToSetting() {
        postSideEffect(HomeSideEffect.NavigateToSetting)
    }

    fun loadPlaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            placeRepository.getPlaces()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    postSideEffect(HomeSideEffect.ShowSnackbar("이미지를 불러오기 실패했습니다."))
                }
                .collectLatest { places ->
                    _uiState.update { it.copy(isLoading = false, places = places) }
                }
        }
    }
}