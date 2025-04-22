package com.wngud.pomosound.ui.presentation.sound

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.pomosound.data.model.SoundCategory
import com.wngud.pomosound.data.model.SoundData
import com.wngud.pomosound.domain.repository.FavoriteRepository
import com.wngud.pomosound.domain.repository.SoundRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

data class SoundUiState(
    val isLoading: Boolean = false,
    val soundCategories: List<SoundCategory> = emptyList(),
    val activePlayers: Map<Int, MediaPlayer> = emptyMap(),
    val selectedSoundIds: Set<Int> = emptySet(),
    val preparedSoundIds: Set<Int> = emptySet(), // Track prepared players
    val soundVolumes: Map<Int, Float> = emptyMap(),
    val isGloballyPaused: Boolean = false,
    val errorMessage: String? = null
) {
    fun isSoundSelected(id: Int): Boolean = selectedSoundIds.contains(id)
    fun isSoundPrepared(id: Int): Boolean = preparedSoundIds.contains(id) // Helper
    val isAnySoundSelected: Boolean get() = selectedSoundIds.isNotEmpty()
    fun getVolume(id: Int): Float = soundVolumes[id] ?: 1.0f
}


sealed class SoundEvent {
    data class SoundItemClicked(val soundData: SoundData) : SoundEvent()
    data class VolumeChanged(val id: Int, val volume: Float) : SoundEvent()
    object GlobalPlayPauseClicked : SoundEvent()
    data class SaveFavorite(val name: String, val sounds: List<Pair<Int, Float>>) : SoundEvent()
}

sealed class SoundSideEffect {
    object NavigateBack : SoundSideEffect()
    data class NavigateToNext(val id: Int) : SoundSideEffect()
    data class ShowSnackbar(val message: String) : SoundSideEffect()
}

@HiltViewModel
class SoundViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val soundRepository: SoundRepository,
    private val favoriteRepository: FavoriteRepository, // Inject FavoriteRepository
    private val application: Application
) : ViewModel() {
    val placeId: Int = savedStateHandle.get<Int>(PLACE_ID_SAVED_STATE_KEY)!!

    private val _uiState = MutableStateFlow(SoundUiState())
    val uiState: StateFlow<SoundUiState> = _uiState.asStateFlow()

    val isPlaying: StateFlow<Boolean> = _uiState
        .map { it.isAnySoundSelected && !it.isGloballyPaused }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _sideEffects = Channel<SoundSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        loadSounds()
    }

    fun postEvent(event: SoundEvent) {
        when (event) {
            is SoundEvent.SoundItemClicked -> handleSoundClick(event.soundData)
            is SoundEvent.VolumeChanged -> handleVolumeChange(event.id, event.volume)
            SoundEvent.GlobalPlayPauseClicked -> handleGlobalPlayPauseClick()
            is SoundEvent.SaveFavorite -> saveFavorite(event.name, event.sounds)
        }
    }

    private fun handleSoundClick(soundData: SoundData) {
        val soundId = soundData.id
        val currentState = _uiState.value
        val isSelected = currentState.isSoundSelected(soundId)

        if (isSelected) {
            deselectSound(soundId)
        } else {
            selectSound(soundData) // This prepares the player
            // If globally paused, resuming will handle starting the newly prepared player too
            if (currentState.isGloballyPaused) {
                // Don't immediately resume here, let the user press play or select another sound
                // The sound is selected, prepared, but remains paused with others.
                 Log.d(TAG, "Selected sound $soundId while paused. It's prepared but waiting for global resume.")
            }
            // If not globally paused, the OnPreparedListener in selectSound will start it when ready
        }
    }

    private fun selectSound(soundData: SoundData) {
        val soundId = soundData.id
        val soundRes = soundData.soundResId
        val context = application.applicationContext
        val currentVolume = _uiState.value.getVolume(soundId)

        _uiState.value.activePlayers[soundId]?.release()
        // Ensure ID is removed from prepared set if we are re-selecting
        _uiState.update { it.copy(preparedSoundIds = it.preparedSoundIds - soundId) }


        try {
            val mediaPlayer = MediaPlayer().apply {
                val uri = "android.resource://${context.packageName}/$soundRes".toUri()
                setDataSource(context, uri)
                isLooping = true
                setVolume(currentVolume, currentVolume)
                setOnPreparedListener { player ->
                    Log.d(TAG, "Player $soundId prepared.")
                    // Mark as prepared
                    _uiState.update {
                        it.copy(preparedSoundIds = it.preparedSoundIds + soundId)
                    }
                    // Start ONLY if not globally paused *at the time preparation finishes*.
                    if (!_uiState.value.isGloballyPaused) {
                         try {
                            if (!player.isPlaying) {
                                player.start()
                                Log.d(TAG, "Player $soundId started by OnPreparedListener (not paused).")
                            }
                         } catch (e: IllegalStateException) {
                             Log.e(TAG, "Error starting player $soundId in OnPreparedListener: ${e.message}")
                             // If start fails here, deselect might be appropriate
                             deselectSound(soundId)
                         }
                    } else {
                         Log.d(TAG, "Player $soundId prepared, but globally paused.")
                    }
                 }
                setOnErrorListener { _, _, _ ->
                    Log.e(TAG, "MediaPlayer Error for soundId $soundId")
                    postSideEffect(SoundSideEffect.ShowSnackbar("Error with sound: ${soundData.name}"))
                    // Clean up state thoroughly on error
                    deselectSound(soundId) // This will also remove from preparedSoundIds
                    true
                }
                prepareAsync()
                Log.d(TAG, "Preparing player $soundId...")
            }

            // Update state immediately to add player and mark as selected
            _uiState.update {
                it.copy(
                    selectedSoundIds = it.selectedSoundIds + soundId,
                    activePlayers = it.activePlayers + (soundId to mediaPlayer),
                    soundVolumes = if (!it.soundVolumes.containsKey(soundId)) {
                        it.soundVolumes + (soundId to 1.0f)
                    } else {
                        it.soundVolumes
                    }
                )
            }
        } catch (e: IOException) {
            postSideEffect(SoundSideEffect.ShowSnackbar("Could not load sound: ${soundData.name}"))
             _uiState.update { it.copy(selectedSoundIds = it.selectedSoundIds - soundId) } // Clean up selection
        } catch (e: IllegalStateException) {
             postSideEffect(SoundSideEffect.ShowSnackbar("Error setting up sound: ${soundData.name}"))
             _uiState.update { it.copy(selectedSoundIds = it.selectedSoundIds - soundId) } // Clean up selection
        }
    }

    private fun deselectSound(soundId: Int) {
        _uiState.value.activePlayers[soundId]?.apply {
            try {
                if (isPlaying) stop()
                release()
                Log.d(TAG,"Released player $soundId")
            } catch (e: IllegalStateException) {
                Log.w(TAG, "Error stopping/releasing player for $soundId: ${e.message}")
            }
        }
        _uiState.update {
            it.copy(
                selectedSoundIds = it.selectedSoundIds - soundId,
                preparedSoundIds = it.preparedSoundIds - soundId, // Remove from prepared set
                activePlayers = it.activePlayers - soundId
            )
        }
    }


    private fun handleGlobalPlayPauseClick() {
        val currentState = _uiState.value
        if (currentState.isAnySoundSelected) {
            if (currentState.isGloballyPaused) {
                resumeAllSelectedSounds()
            } else {
                pauseAllSelectedSounds()
            }
        }
    }

    private fun pauseAllSelectedSounds() {
        Log.d(TAG, "Pausing all selected sounds.")
        var actuallyPaused = false
        _uiState.value.activePlayers.forEach { (id, mediaPlayer) ->
             if (_uiState.value.isSoundSelected(id)) {
                try {
                    // Check if prepared before trying to pause
                    if (_uiState.value.isSoundPrepared(id) && mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        Log.d(TAG, "Paused player $id.")
                        actuallyPaused = true
                    } else if (!_uiState.value.isSoundPrepared(id)) {
                         Log.w(TAG, "Attempted to pause player $id but it wasn't prepared.")
                    }
                } catch (e: IllegalStateException) {
                     Log.w(TAG, "Error pausing player $id: ${e.message}")
                }
             }
        }
        // Only set globally paused if we actually paused something or if sounds are selected
        if (actuallyPaused || _uiState.value.isAnySoundSelected) {
             _uiState.update { it.copy(isGloballyPaused = true) }
        }
    }

    private fun resumeAllSelectedSounds() {
        Log.d(TAG, "Resuming all selected sounds.")
        _uiState.update { it.copy(isGloballyPaused = false) } // Set paused to false first
        _uiState.value.activePlayers.forEach { (id, mediaPlayer) ->
            if (_uiState.value.isSoundSelected(id)) { // Ensure it's a selected sound
                try {
                    // Crucially, check if it's prepared before trying to start
                    if (_uiState.value.isSoundPrepared(id) && !mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                        Log.d(TAG, "Attempted to start prepared player $id in resumeAllSelectedSounds")
                    } else if (!_uiState.value.isSoundPrepared(id)) {
                         Log.w(TAG, "Attempted to resume player $id but it wasn't prepared yet.")
                         // Player will start via OnPreparedListener when ready, since isGloballyPaused is now false.
                    }
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "Error starting player $id in resumeAllSelectedSounds: ${e.message}")
                    // Consider deselecting or notifying user
                    deselectSound(id)
                }
            }
        }
    }

    private fun handleVolumeChange(soundId: Int, volume: Float) {
        val clampedVolume = volume.coerceIn(0f, 1f)
        // Check if player exists before trying to set volume
        _uiState.value.activePlayers[soundId]?.let { player ->
             try {
                 player.setVolume(clampedVolume, clampedVolume)
             } catch (e: IllegalStateException) {
                 Log.e(TAG, "Error setting volume for player $soundId: ${e.message}")
             }
        }
        // Update volume in state regardless of player state
        _uiState.update {
            it.copy(soundVolumes = it.soundVolumes + (soundId to clampedVolume))
        }
    }


    private fun postSideEffect(sideEffect: SoundSideEffect) {
        viewModelScope.launch {
            _sideEffects.send(sideEffect)
        }
    }

    fun navigateToBack() {
        postSideEffect(SoundSideEffect.NavigateBack)
    }

    fun navigateToNext(id: Int) {
        postSideEffect(SoundSideEffect.NavigateToNext(id))
    }

    private fun loadSounds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            soundRepository.getSounds()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    postSideEffect(SoundSideEffect.ShowSnackbar("소리를 불러오기 실패했습니다."))
                }
                .collectLatest { sounds ->
                    _uiState.update { it.copy(isLoading = false, soundCategories = sounds) }
                }
        }
    }

    private fun saveFavorite(name: String, sounds: List<Pair<Int, Float>>) {
        viewModelScope.launch {
            try {
                favoriteRepository.saveFavorite(name, sounds)
                postSideEffect(SoundSideEffect.ShowSnackbar("'$name' 즐겨찾기에 저장되었습니다."))
            } catch (e: Exception) {
                Log.e(TAG, "Error saving favorite '$name': ${e.message}", e)
                postSideEffect(SoundSideEffect.ShowSnackbar("즐겨찾기 저장 실패"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.value.activePlayers.values.forEach { mediaPlayer ->
            try {
                 if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                 }
                 mediaPlayer.release()
            } catch (e: IllegalStateException) {
                 // Ignore errors during cleanup
            }
        }
         _uiState.update { it.copy(activePlayers = emptyMap(), selectedSoundIds = emptySet(), preparedSoundIds = emptySet()) } // Clear prepared set too
    }


    companion object {
        private const val PLACE_ID_SAVED_STATE_KEY = "placeId"
        private const val TAG = "SoundViewModel" // Added for logging
    }
}
