package com.comunidadedevspace.imc.core.network

import com.comunidadedevspace.imc.core.config.FeatureFlagKeys
import com.comunidadedevspace.imc.core.config.FeatureFlagManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class CircuitBreakerInterceptor
    @Inject
    constructor(
        private val featureFlagManager: FeatureFlagManager,
    ) : Interceptor {
        private val failureCount = AtomicInteger(0)
        private var isOpen: Boolean = false
        private var lastFailureTimestamp: Long = 0

        override fun intercept(chain: Interceptor.Chain): Response {
            if (!featureFlagManager.isEnabled(FeatureFlagKeys.CIRCUIT_BREAKER_ENABLED)) {
                return chain.proceed(chain.request())
            }

            val now = System.currentTimeMillis()
            if (isOpen && now - lastFailureTimestamp < RESET_TIMEOUT_MS) {
                throw IOException("Circuit breaker is open")
            } else if (isOpen) {
                isOpen = false
                failureCount.set(0)
            }

            return try {
                val response = chain.proceed(chain.request())
                if (!response.isSuccessful) {
                    registerFailure()
                } else {
                    failureCount.set(0)
                }
                response
            } catch (ioe: IOException) {
                registerFailure()
                throw ioe
            }
        }

        private fun registerFailure() {
            val count = failureCount.incrementAndGet()
            lastFailureTimestamp = System.currentTimeMillis()
            if (count >= FAILURE_THRESHOLD) {
                isOpen = true
                Timber.e("Circuit breaker opened after %d failures", count)
            }
        }

        companion object {
            private const val FAILURE_THRESHOLD = 3
            private const val RESET_TIMEOUT_MS = 10_000L
        }
    }
