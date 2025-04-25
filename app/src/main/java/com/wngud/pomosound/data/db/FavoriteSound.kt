package com.wngud.pomosound.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_sounds")
data class FavoriteSound(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
