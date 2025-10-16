package com.comunidadedevspace.imc.app.update

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppUpdateManager
    @Inject
    constructor(
        private val appUpdateManager: AppUpdateManager,
    ) {
        fun checkForUpdates(activity: Activity) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        AppUpdateType.FLEXIBLE,
                        activity,
                        REQUEST_CODE,
                    )
                }
            }
        }

        companion object {
            private const val REQUEST_CODE = 1001
        }
    }

@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
object InAppUpdateModule {
    @dagger.Provides
    @Singleton
    fun provideAppUpdateManager(application: android.app.Application): AppUpdateManager = AppUpdateManagerFactory.create(application)
}
