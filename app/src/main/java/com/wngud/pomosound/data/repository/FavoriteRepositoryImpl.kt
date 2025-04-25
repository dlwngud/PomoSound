package com.wngud.pomosound.data.repository

import com.wngud.pomosound.data.db.FavoriteSound
import com.wngud.pomosound.data.db.FavoriteSoundDao
import com.wngud.pomosound.data.db.FavoriteSoundItem
import com.wngud.pomosound.data.db.FavoriteSoundWithItems
import com.wngud.pomosound.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteSoundDao: FavoriteSoundDao
) : FavoriteRepository {

    override suspend fun saveFavorite(name: String, sounds: List<Pair<Int, Float>>) {
        // Create the main FavoriteSound entity (without the list)
        val favoriteSound = FavoriteSound(name = name)
        // Create the list of FavoriteSoundItem entities from the input pairs
        // Note: favoriteSoundId will be set correctly within the DAO transaction
        val favoriteItems = sounds.map { (soundId, volume) ->
            FavoriteSoundItem(favoriteSoundId = 0, soundId = soundId, volume = volume)
        }
        // Call the DAO's transaction method
        favoriteSoundDao.addFavoriteSound(favoriteSound, favoriteItems)
    }

    override fun getAllFavorites(): Flow<List<FavoriteSoundWithItems>> {
        // Call the updated DAO method
        return favoriteSoundDao.getAllFavoritesWithItems()
    }

    // Add delete functionality if needed, using the DAO's delete method
    override suspend fun deleteFavorite(favoriteSound: FavoriteSound) {
        favoriteSoundDao.deleteFavorite(favoriteSound)
    }
}
