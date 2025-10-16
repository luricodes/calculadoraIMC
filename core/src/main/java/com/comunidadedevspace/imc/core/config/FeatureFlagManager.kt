package com.comunidadedevspace.imc.core.config

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeatureFlagManager
    @Inject
    constructor(
        private val sharedPreferences: SharedPreferences,
    ) {
        fun isEnabled(flag: String): Boolean = sharedPreferences.getBoolean(flag, DEFAULT_FLAGS[flag] ?: false)

        fun setFlag(
            flag: String,
            enabled: Boolean,
        ) {
            sharedPreferences.edit { putBoolean(flag, enabled) }
        }

        fun reset() {
            sharedPreferences.edit { clear() }
        }

        companion object {
            private val DEFAULT_FLAGS =
                mapOf(
                    FeatureFlagKeys.AUTH_ENABLED to true,
                    FeatureFlagKeys.OFFLINE_SYNC_ENABLED to true,
                    FeatureFlagKeys.UPLOAD_ENABLED to true,
                    FeatureFlagKeys.CIRCUIT_BREAKER_ENABLED to true,
                    FeatureFlagKeys.DARK_MODE_TOGGLE to true,
                )
        }
    }
