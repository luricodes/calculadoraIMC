package com.comunidadedevspace.imc.core.sync

import com.comunidadedevspace.imc.core.database.dao.ImcRecordDao
import com.comunidadedevspace.imc.core.database.entity.ImcRecordEntity
import com.comunidadedevspace.imc.core.util.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
        private val scope = CoroutineScope(dispatcherProvider.io + SupervisorJob())

        fun syncPendingRecords(syncAction: suspend (List<ImcRecordEntity>) -> Result<Unit>) {
            scope.launch {
                val pending = imcRecordDao.getPendingSync()
                if (pending.isEmpty()) return@launch
                val result = syncAction(pending)
                result.onSuccess {
                    imcRecordDao.markSynced(pending.map { it.id }, true)
                }.onFailure {
                    Timber.e(it, "Failed to sync pending records")
                }
            }
        }
    }
