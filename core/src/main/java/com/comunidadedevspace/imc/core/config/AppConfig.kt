package com.comunidadedevspace.imc.core.config

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized runtime configuration that can be overridden using remote config
 * or developer settings.
 */
@Singleton
class AppConfig
    @Inject
    constructor(
        private val featureFlagManager: FeatureFlagManager,
        private val remoteConfigManager: RemoteConfigManager,
        @ApplicationContext private val context: Context,
    ) {
        /** Default API endpoint – override through Remote Config (`api_base_url`). */
        var baseUrl: String = DEFAULT_BASE_URL
            private set

        /** Cold start target in milliseconds (documented in RUNBOOK). */
        val coldStartTargetMs: Long = 2000L

        suspend fun refresh() {
            remoteConfigManager.fetchAndActivate()
            baseUrl = remoteConfigManager.getString(KEY_API_BASE_URL) ?: DEFAULT_BASE_URL
        }

        fun isFeatureEnabled(key: String): Boolean = featureFlagManager.isEnabled(key)

        fun setFeatureEnabled(
            key: String,
            enabled: Boolean,
        ) {
            featureFlagManager.setFlag(key, enabled)
        }

        fun resetToDefaults() {
            featureFlagManager.reset()
            baseUrl = DEFAULT_BASE_URL
        }

        fun provideUserAgent(): String = "IMCApp/${BuildInfo.versionName} (${context.packageName})"

        companion object {
            const val KEY_API_BASE_URL = "api_base_url"
            private const val DEFAULT_BASE_URL = "https://api.example.com"
        }
    }
