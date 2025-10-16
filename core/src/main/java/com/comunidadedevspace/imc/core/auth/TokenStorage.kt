package com.comunidadedevspace.imc.core.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val masterKey =
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        private val prefs =
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )

        fun saveTokens(
            accessToken: String,
            refreshToken: String,
        ) {
            prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply()
        }

        fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

        fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

        fun clear() {
            prefs.edit().clear().apply()
        }

        companion object {
            private const val PREFS_NAME = "secure_tokens"
            private const val KEY_ACCESS_TOKEN = "access_token"
            private const val KEY_REFRESH_TOKEN = "refresh_token"
        }
    }
