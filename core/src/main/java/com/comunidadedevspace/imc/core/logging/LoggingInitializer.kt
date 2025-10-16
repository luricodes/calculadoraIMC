package com.comunidadedevspace.imc.core.logging

import android.app.Application
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInitializer
    @Inject
    constructor() {
        fun init(
            application: Application,
            crashReportingTree: Timber.Tree? = null,
        ) {
            if (Timber.forest().isEmpty()) {
                if (crashReportingTree != null) {
                    Timber.plant(crashReportingTree)
                }
                Timber.plant(Timber.DebugTree())
            }
            Timber.i("Logging initialized for %s", application.packageName)
        }
    }
