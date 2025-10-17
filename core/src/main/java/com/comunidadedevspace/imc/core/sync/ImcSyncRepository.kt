package com.comunidadedevspace.imc.core.sync

import com.comunidadedevspace.imc.core.database.dao.ImcRecordDao
import com.comunidadedevspace.imc.core.database.entity.ImcRecordEntity
import com.comunidadedevspace.imc.core.util.DispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImcSyncRepository
    @Inject
    constructor(
        private val imcRecordDao: ImcRecordDao,
        private val dispatcherProvider: DispatcherProvider,
    ) {
        suspend fun syncPendingRecords(syncAction: suspend (List<ImcRecordEntity>) -> Result<Unit>): Result<Unit> =
            withContext(dispatcherProvider.io) {
                val pending = imcRecordDao.getPendingSync()
                if (pending.isEmpty()) {
                    Timber.i("No pending records to sync")
                    return@withContext Result.success(Unit)
                }
                Timber.i("Attempting to sync ${'$'}{pending.size} pending records")
                val result = syncAction(pending)
                result.onSuccess {
                    imcRecordDao.markSynced(pending.map { it.id }, true)
                }.onFailure {
                    Timber.e(it, "Failed to sync pending records")
                }
                result
            }
    }
