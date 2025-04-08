package com.wngud.pomosound.data.repository

import com.wngud.pomosound.data.model.PlaceItemData
import com.wngud.pomosound.data.source.PlaceDataSource
import com.wngud.pomosound.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val dataSource: PlaceDataSource
) : PlaceRepository {
    override fun getPlaces(): Flow<List<PlaceItemData>> {
        return flow {
            emit(dataSource.places)
        }
    }
}