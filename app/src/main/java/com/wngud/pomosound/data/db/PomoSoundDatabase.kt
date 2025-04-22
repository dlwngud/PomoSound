package com.wngud.pomosound.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FavoriteSound::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PomoSoundDatabase : RoomDatabase() {
    abstract fun favoriteSoundDao(): FavoriteSoundDao
}
