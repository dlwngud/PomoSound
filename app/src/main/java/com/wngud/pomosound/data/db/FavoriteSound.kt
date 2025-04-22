package com.wngud.pomosound.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "favorite_sounds")
@TypeConverters(Converters::class)
data class FavoriteSound(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val sounds: List<Pair<Int, Float>> // List of (Sound ID, Volume)
)
