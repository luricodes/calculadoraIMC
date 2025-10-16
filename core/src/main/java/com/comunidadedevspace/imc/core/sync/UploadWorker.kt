package com.comunidadedevspace.imc.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.comunidadedevspace.imc.core.upload.FileUploadRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class UploadWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParams: WorkerParameters,
        private val fileUploadRepository: FileUploadRepository,
    ) : CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {
            val url = inputData.getString(KEY_PRESIGNED_URL) ?: return Result.failure()
            val path = inputData.getString(KEY_FILE_URI) ?: return Result.failure()
            val mime = inputData.getString(KEY_MIME) ?: "application/octet-stream"
            return fileUploadRepository.uploadPresigned(android.net.Uri.parse(path), url, mime)
                .fold(
                    onSuccess = {
                        Timber.i("Upload succeeded")
                        Result.success()
                    },
                    onFailure = {
                        Timber.e(it, "Upload failed")
                        Result.retry()
                    },
                )
        }

        companion object {
            const val KEY_PRESIGNED_URL = "presigned_url"
            const val KEY_FILE_URI = "file_uri"
            const val KEY_MIME = "mime_type"
        }
    }
