package com.wngud.pomosound.data.repository

import com.wngud.pomosound.data.db.FavoriteSound
import com.wngud.pomosound.data.db.FavoriteSoundDao
import com.wngud.pomosound.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteSoundDao: FavoriteSoundDao
) : FavoriteRepository {

    override suspend fun saveFavorite(name: String, sounds: List<Pair<Int, Float>>) {
        val favoriteSound = FavoriteSound(name = name, sounds = sounds)
        favoriteSoundDao.insertFavorite(favoriteSound)
    }

    override fun getAllFavorites(): Flow<List<FavoriteSound>> {
        return favoriteSoundDao.getAllFavorites()
    }
}
