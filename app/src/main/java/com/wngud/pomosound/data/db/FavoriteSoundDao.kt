package com.wngud.pomosound.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteSound: FavoriteSound)

    @Query("SELECT * FROM favorite_sounds ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<FavoriteSound>>

    // Optional: Add delete functionality if needed later
    // @Delete
    // suspend fun deleteFavorite(favoriteSound: FavoriteSound)

    // Optional: Get by ID if needed
    // @Query("SELECT * FROM favorite_sounds WHERE id = :id")
    // suspend fun getFavoriteById(id: Int): FavoriteSound?
}
