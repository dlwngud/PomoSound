package com.wngud.pomosound.di

import com.wngud.pomosound.data.repository.FavoriteRepositoryImpl
import com.wngud.pomosound.data.repository.PlaceRepositoryImpl
import com.wngud.pomosound.data.repository.SoundRepositoryImpl
import com.wngud.pomosound.domain.repository.FavoriteRepository
import com.wngud.pomosound.domain.repository.PlaceRepository
import com.wngud.pomosound.domain.repository.SoundRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(placeRepositoryImpl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindSoundRepository(soundRepositoryImpl: SoundRepositoryImpl): SoundRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository
}
