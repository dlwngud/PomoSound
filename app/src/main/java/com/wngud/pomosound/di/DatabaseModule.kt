package com.wngud.pomosound.di

import android.content.Context
import androidx.room.Room
import com.wngud.pomosound.data.db.FavoriteSoundDao
import com.wngud.pomosound.data.db.PomoSoundDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePomoSoundDatabase(@ApplicationContext context: Context): PomoSoundDatabase {
        return Room.databaseBuilder(
            context,
            PomoSoundDatabase::class.java,
            "pomosound_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteSoundDao(database: PomoSoundDatabase): FavoriteSoundDao {
        return database.favoriteSoundDao()
    }
}
