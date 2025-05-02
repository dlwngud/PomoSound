package com.wngud.pomosound.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

// Data class to hold the parent FavoriteSound and its related FavoriteSoundItems
data class FavoriteSoundWithItems(
    @Embedded val favoriteSound: FavoriteSound,
    @Relation(
        parentColumn = "id",
        entityColumn = "favoriteSoundId"
    )
    val items: List<FavoriteSoundItem>
)

@Dao
interface FavoriteSoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteSound(favoriteSound: FavoriteSound): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItems(items: List<FavoriteSoundItem>)

    @Transaction
    suspend fun addFavoriteSound(favoriteSound: FavoriteSound, items: List<FavoriteSoundItem>) {
        val favoriteId = insertFavoriteSound(favoriteSound)
        val itemsWithForeignKey = items.map { it.copy(favoriteSoundId = favoriteId.toInt()) }
        insertFavoriteItems(itemsWithForeignKey)
    }

    @Transaction // Ensures the read operation is atomic
    @Query("SELECT * FROM favorite_sounds ORDER BY id DESC")
    fun getAllFavoritesWithItems(): Flow<List<FavoriteSoundWithItems>>

    @Delete
    suspend fun deleteFavorite(favoriteSound: FavoriteSound)
}
