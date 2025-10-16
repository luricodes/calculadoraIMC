package com.comunidadedevspace.imc.core.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T,
): Result<T> =
    withContext(dispatcher) {
        try {
            Result.success(block())
        } catch (http: HttpException) {
            Timber.e(http, "HTTP error")
            Result.failure(http)
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Unknown network error")
            Result.failure(throwable)
        }
    }
