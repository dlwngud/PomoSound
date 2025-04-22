package com.wngud.pomosound.domain.repository

import com.wngud.pomosound.data.db.FavoriteSound
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun saveFavorite(name: String, sounds: List<Pair<Int, Float>>)
    fun getAllFavorites(): Flow<List<FavoriteSound>>
    // Add other methods like delete if needed later
}
