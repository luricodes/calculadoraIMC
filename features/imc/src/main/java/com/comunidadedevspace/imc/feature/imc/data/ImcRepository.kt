package com.comunidadedevspace.imc.feature.imc.data

import com.comunidadedevspace.imc.feature.imc.domain.ImcRecord
import kotlinx.coroutines.flow.Flow

interface ImcRepository {
    fun observeHistory(): Flow<List<ImcRecord>>

    suspend fun persist(
        weightKg: Double,
        heightMeters: Double,
    ): ImcRecord

    suspend fun scheduleSync(record: ImcRecord)
}
