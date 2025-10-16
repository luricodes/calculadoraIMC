package com.comunidadedevspace.imc.core.upload

import android.net.Uri
import com.comunidadedevspace.imc.core.network.safeApiCall
import com.comunidadedevspace.imc.core.util.DispatcherProvider
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUploadRepository
    @Inject
    constructor(
        private val okHttpClient: OkHttpClient,
        private val dispatcherProvider: DispatcherProvider,
    ) {
        suspend fun uploadPresigned(
            uri: Uri,
            presignedUrl: String,
            mimeType: String,
        ): Result<Unit> =
            withContext(dispatcherProvider.io) {
                val file = File(uri.path ?: return@withContext Result.failure(IllegalArgumentException("Invalid file uri")))
                if (!file.exists()) return@withContext Result.failure(IllegalArgumentException("File missing"))
                if (file.length() > MAX_UPLOAD_BYTES) {
                    return@withContext Result.failure(IllegalStateException("File too large"))
                }
                val body = file.asRequestBody(mimeType.toMediaTypeOrNull())
                val request =
                    Request.Builder()
                        .url(presignedUrl)
                        .put(body)
                        .build()
                safeApiCall(dispatcherProvider.io) {
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw IllegalStateException("Upload failed: ${response.code}")
                    }
                }
            }

        companion object {
            private const val MAX_UPLOAD_BYTES = 15L * 1024 * 1024
        }
    }
