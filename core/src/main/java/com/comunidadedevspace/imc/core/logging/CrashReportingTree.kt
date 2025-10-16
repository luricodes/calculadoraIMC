package com.comunidadedevspace.imc.core.logging

import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        // TODO: Hook into Crashlytics or Sentry SDK once configured externally.
    }
}
