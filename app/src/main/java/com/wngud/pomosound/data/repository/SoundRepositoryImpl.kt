package com.wngud.pomosound.data.repository

import com.wngud.pomosound.data.model.SoundCategory
import com.wngud.pomosound.data.source.SoundDataSource
import com.wngud.pomosound.domain.repository.SoundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SoundRepositoryImpl @Inject constructor(
    private val dataSource: SoundDataSource
): SoundRepository {
    override fun getSounds(): Flow<List<SoundCategory>> {
        return flow {
            emit(dataSource.soundCategories)
        }
    }
}