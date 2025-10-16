package com.comunidadedevspace.imc.core.sync

import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncScheduler
    @Inject
    constructor(
        private val workManager: WorkManager,
    ) {
        fun enqueuePeriodicSync() {
            val request =
                PeriodicWorkRequestBuilder<SyncWorker>(Duration.ofHours(6))
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(15))
                    .build()
            workManager.enqueueUniquePeriodicWork(
                SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request,
            )
        }

        fun enqueueImmediateSync() {
            val request =
                OneTimeWorkRequestBuilder<SyncWorker>()
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(5))
                    .build()
            workManager.enqueueUniqueWork(
                SYNC_ONCE_WORK_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request,
            )
        }

        fun enqueueUpload(
            presignedUrl: String,
            fileUri: String,
            mimeType: String,
        ) {
            val request =
                OneTimeWorkRequestBuilder<UploadWorker>()
                    .setInputData(
                        androidx.work.Data.Builder()
                            .putString(UploadWorker.KEY_PRESIGNED_URL, presignedUrl)
                            .putString(UploadWorker.KEY_FILE_URI, fileUri)
                            .putString(UploadWorker.KEY_MIME, mimeType)
                            .build(),
                    )
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(2))
                    .build()
            workManager.enqueueUniqueWork(
                "upload-$fileUri",
                ExistingWorkPolicy.REPLACE,
                request,
            )
        }

        companion object {
            private const val SYNC_WORK_NAME = "periodic_sync"
            private const val SYNC_ONCE_WORK_NAME = "sync_once"
        }
    }
