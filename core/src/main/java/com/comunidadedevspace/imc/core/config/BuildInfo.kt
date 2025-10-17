package com.comunidadedevspace.imc.core.config

import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildInfo
    @Inject
    constructor(
        @ApplicationContext private val context: android.content.Context,
    ) {
        private val packageInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0),
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
        val versionName: String = packageInfo.versionName ?: "0.0.0"
        val versionCode: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                (packageInfo.longVersionCode and 0xFFFFFFFF).toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
    }
