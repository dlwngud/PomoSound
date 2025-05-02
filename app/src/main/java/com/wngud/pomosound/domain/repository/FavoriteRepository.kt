package com.wngud.pomosound.domain.repository

import com.wngud.pomosound.data.db.FavoriteSound
import com.wngud.pomosound.data.db.FavoriteSoundWithItems
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun saveFavorite(name: String, sounds: List<Pair<Int, Float>>)
    fun getAllFavorites(): Flow<List<FavoriteSoundWithItems>> // Updated return type
    suspend fun deleteFavorite(favoriteSound: FavoriteSound) // Added delete method
}
