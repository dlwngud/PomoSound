package com.wngud.pomosound.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_sound_items",
    foreignKeys = [ForeignKey(
        entity = FavoriteSound::class,
        parentColumns = ["id"],
        childColumns = ["favoriteSoundId"],
        onDelete = ForeignKey.CASCADE // If a FavoriteSound is deleted, its items are also deleted
    )],
    indices = [Index("favoriteSoundId")] // Index for faster lookups by favoriteSoundId
)
data class FavoriteSoundItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val favoriteSoundId: Int, // Foreign key linking to FavoriteSound
    val soundId: Int,         // The actual sound resource ID
    val volume: Float         // The volume for this sound
)
