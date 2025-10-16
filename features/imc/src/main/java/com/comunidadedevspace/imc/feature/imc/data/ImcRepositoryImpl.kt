package com.comunidadedevspace.imc.feature.imc.data

import com.comunidadedevspace.imc.core.config.FeatureFlagKeys
import com.comunidadedevspace.imc.core.config.FeatureFlagManager
import com.comunidadedevspace.imc.core.database.dao.ImcRecordDao
import com.comunidadedevspace.imc.core.database.entity.ImcRecordEntity
import com.comunidadedevspace.imc.core.sync.SyncScheduler
import com.comunidadedevspace.imc.core.util.DispatcherProvider
import com.comunidadedevspace.imc.feature.imc.domain.CalculateImcUseCase
import com.comunidadedevspace.imc.feature.imc.domain.ImcClassification
import com.comunidadedevspace.imc.feature.imc.domain.ImcRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImcRepositoryImpl
    @Inject
    constructor(
        private val imcRecordDao: ImcRecordDao,
        private val dispatcherProvider: DispatcherProvider,
        private val calculateImc: CalculateImcUseCase,
        private val syncScheduler: SyncScheduler,
        private val featureFlagManager: FeatureFlagManager,
    ) : ImcRepository {
        override fun observeHistory(): Flow<List<ImcRecord>> =
            imcRecordDao.observeRecords().map {
                    entities ->
                entities.map { it.toDomain() }
            }

        override suspend fun persist(
            weightKg: Double,
            heightMeters: Double,
        ): ImcRecord =
            withContext(dispatcherProvider.io) {
                val (bmi, classification) = calculateImc(weightKg, heightMeters)
                val entity =
                    ImcRecordEntity(
                        weightKg = weightKg,
                        heightMeters = heightMeters,
                        bmi = bmi,
                        classification = classification.name,
                        recordedAt = Instant.now(),
                        synced = false,
                    )
                val id = imcRecordDao.insert(entity)
                entity.copy(id = id).toDomain(classification)
            }

        override suspend fun scheduleSync(record: ImcRecord) {
            if (featureFlagManager.isEnabled(FeatureFlagKeys.OFFLINE_SYNC_ENABLED)) {
                syncScheduler.enqueueImmediateSync()
            }
        }

        private fun ImcRecordEntity.toDomain(classification: ImcClassification? = null): ImcRecord {
            val resolvedClassification = classification ?: ImcClassification.valueOf(this.classification)
            return ImcRecord(
                id = id,
                weightKg = weightKg,
                heightMeters = heightMeters,
                bmi = bmi,
                classification = resolvedClassification,
                recordedAt = recordedAt,
                synced = synced,
            )
        }
    }
