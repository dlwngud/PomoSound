package com.wngud.pomosound.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSound::class, FavoriteSoundItem::class], version = 2, exportSchema = false)
abstract class PomoSoundDatabase : RoomDatabase() {
    abstract fun favoriteSoundDao(): FavoriteSoundDao
}
