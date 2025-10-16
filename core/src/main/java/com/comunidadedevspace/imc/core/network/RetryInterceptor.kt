package com.comunidadedevspace.imc.core.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class RetryInterceptor
    @Inject
    constructor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var attempt = 0
            var waitTime = INITIAL_BACKOFF_MS
            var lastException: IOException? = null
            while (attempt < MAX_ATTEMPTS) {
                try {
                    return chain.proceed(chain.request())
                } catch (ioe: IOException) {
                    lastException = ioe
                    Timber.w(ioe, "Request failed. Attempt=%d", attempt + 1)
                    if (attempt == MAX_ATTEMPTS - 1) break
                    Thread.sleep(waitTime)
                    waitTime *= BACKOFF_MULTIPLIER
                    attempt++
                }
            }
            throw lastException ?: IOException("Unknown network error")
        }

        companion object {
            private const val MAX_ATTEMPTS = 3
            private const val INITIAL_BACKOFF_MS = 250L
            private const val BACKOFF_MULTIPLIER = 2L
        }
    }
