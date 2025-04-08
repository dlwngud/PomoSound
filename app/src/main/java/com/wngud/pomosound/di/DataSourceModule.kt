package com.wngud.pomosound.di

import com.wngud.pomosound.data.source.PlaceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun bindPlaceRepository(): PlaceDataSource = PlaceDataSource
}