package com.wngud.pomosound.domain.repository

import com.wngud.pomosound.data.model.PlaceItemData
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    fun getPlaces(): Flow<List<PlaceItemData>>

    fun getPlaceById(id: Int): Flow<PlaceItemData>
}