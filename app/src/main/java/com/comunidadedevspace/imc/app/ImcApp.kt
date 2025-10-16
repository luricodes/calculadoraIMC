package com.comunidadedevspace.imc.app

import android.app.Application
import com.comunidadedevspace.imc.core.config.AppConfig
import com.comunidadedevspace.imc.core.logging.CrashReportingTree
import com.comunidadedevspace.imc.core.logging.LoggingInitializer
import com.comunidadedevspace.imc.core.sync.SyncScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ImcApp : Application() {
    @Inject
    lateinit var loggingInitializer: LoggingInitializer

    @Inject
    lateinit var syncScheduler: SyncScheduler

    @Inject
    lateinit var appConfig: AppConfig

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        loggingInitializer.init(this, CrashReportingTree())
        applicationScope.launch {
            runCatching { appConfig.refresh() }
                .onFailure { Timber.e(it, "Failed to refresh config") }
        }
        syncScheduler.enqueuePeriodicSync()
    }
}
