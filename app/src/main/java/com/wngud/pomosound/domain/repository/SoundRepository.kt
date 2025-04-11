package com.wngud.pomosound.domain.repository

import com.wngud.pomosound.data.model.SoundCategory
import kotlinx.coroutines.flow.Flow

interface SoundRepository {
    fun getSounds(): Flow<List<SoundCategory>>
}