package com.wngud.pomosound.ui.presentation.favoriteSound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.pomosound.data.db.FavoriteSound
import com.wngud.pomosound.data.db.FavoriteSoundWithItems
import com.wngud.pomosound.domain.repository.FavoriteRepository
import com.wngud.pomosound.domain.repository.SoundRepository // Import SoundRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteSoundsState(
    val isLoading: Boolean = true,
    val favoriteSounds: List<FavoriteSoundWithItems> = emptyList(),
    val playingSoundId: Int? = null, // ID of the currently playing FavoriteSound
    val error: String? = null,
    // Map from SoundData.id to SoundData.soundResId
    val soundIdToResIdMap: Map<Int, Int> = emptyMap()
)

sealed class FavoriteSoundsIntent {
    data object LoadFavorites : FavoriteSoundsIntent()
    data class PlaySound(val favoriteSound: FavoriteSoundWithItems) : FavoriteSoundsIntent()
    data object PauseSound : FavoriteSoundsIntent()
    data class DeleteSound(val favoriteSound: FavoriteSound) : FavoriteSoundsIntent()
}


@HiltViewModel
class FavoriteSoundsViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val soundRepository: SoundRepository // Inject SoundRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteSoundsState())
    val state: StateFlow<FavoriteSoundsState> = _state.asStateFlow()

    init {
        loadSoundsAndFavorites() // Load both sounds and favorites
    }

    fun handleIntent(intent: FavoriteSoundsIntent) {
        when (intent) {
            is FavoriteSoundsIntent.LoadFavorites -> {} //  loadFavoriteSounds() // No longer needed, loaded in init
            is FavoriteSoundsIntent.PlaySound -> playSound(intent.favoriteSound)
            is FavoriteSoundsIntent.PauseSound -> pauseSound()
            is FavoriteSoundsIntent.DeleteSound -> deleteSound(intent.favoriteSound)
        }
    }

    // Combines loading sounds and favorites
    private fun loadSoundsAndFavorites() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Load sounds first
            soundRepository.getSounds()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Failed to load sounds") }
                }
                .collect { soundCategories ->
                    // Create the soundIdToResIdMap
                    val soundIdToResIdMap = soundCategories.flatMap { it.sounds }
                        .associate { it.id to it.soundResId }

                    // Load favorites after sounds are loaded
                    favoriteRepository.getAllFavorites()
                        .catch { e ->
                            _state.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Failed to load favorites") }
                        }
                        .collect { favorites ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    favoriteSounds = favorites,
                                    soundIdToResIdMap = soundIdToResIdMap, // Set the map in the state
                                    playingSoundId = it.playingSoundId?.takeIf { id -> favorites.any { fav -> fav.favoriteSound.id == id } }
                                )
                            }
                        }
                }
        }
    }


    private fun playSound(favoriteSound: FavoriteSoundWithItems) {
        // In a real app, you'd likely interact with a MediaPlayer service here.
        // For MVI state management, we just update the playing ID.
        _state.update { it.copy(playingSoundId = favoriteSound.favoriteSound.id) }
    }

    private fun pauseSound() {
        // Stop playback via service if applicable.
        _state.update { it.copy(playingSoundId = null) }
    }

    private fun deleteSound(favoriteSound: FavoriteSound) {
        viewModelScope.launch {
            try {
                favoriteRepository.deleteFavorite(favoriteSound)
                // The Flow from getAllFavorites will automatically update the list
                // Optionally, show a confirmation message via a side effect channel
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage ?: "Failed to delete favorite") }
                // Log the error
            }
        }
    }
}
