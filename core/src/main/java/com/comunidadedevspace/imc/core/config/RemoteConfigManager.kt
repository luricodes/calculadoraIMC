package com.comunidadedevspace.imc.core.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager
    @Inject
    constructor(
        private val dispatcher: CoroutineDispatcher,
    ) {
        private val cache: MutableMap<String, String> = mutableMapOf()

        suspend fun fetchAndActivate() =
            withContext(dispatcher) {
                Timber.d("RemoteConfig fetch triggered")
                // TODO: Integrate Firebase Remote Config or other provider.
            }

        fun getString(key: String): String? = cache[key]
    }
