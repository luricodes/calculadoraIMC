package com.comunidadedevspace.imc.app

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        private val dataStore =
            PreferenceDataStoreFactory.create(scope = scope) {
                context.preferencesDataStoreFile(FILE_NAME)
            }

        val isDarkTheme: Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[DARK_THEME_KEY] ?: false
            }

        val selectedLocale: Flow<Locale> =
            dataStore.data.map { preferences ->
                preferences[LOCALE_KEY]?.let { Locale.forLanguageTag(it) } ?: Locale.ENGLISH
            }

        suspend fun setDarkTheme(enabled: Boolean) {
            dataStore.edit { preferences ->
                preferences[DARK_THEME_KEY] = enabled
            }
        }

        suspend fun setLocale(locale: Locale) {
            dataStore.edit { preferences ->
                preferences[LOCALE_KEY] = locale.toLanguageTag()
            }
        }

        companion object {
            private const val FILE_NAME = "user_preferences"
            private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
            private val LOCALE_KEY = stringPreferencesKey("locale")
        }
    }
