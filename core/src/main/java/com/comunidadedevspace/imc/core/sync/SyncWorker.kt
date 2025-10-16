package com.comunidadedevspace.imc.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParams: WorkerParameters,
        private val syncRepository: ImcSyncRepository,
    ) : CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {
            Timber.i("Running sync worker")
            return try {
                syncRepository.syncPendingRecords { Result.success(Unit) }
                Result.success()
            } catch (t: Throwable) {
                Timber.e(t, "Sync worker failed")
                Result.retry()
            }
        }
    }
