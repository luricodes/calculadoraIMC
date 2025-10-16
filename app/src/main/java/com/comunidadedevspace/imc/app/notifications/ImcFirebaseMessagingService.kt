package com.comunidadedevspace.imc.app.notifications

import com.comunidadedevspace.imc.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ImcFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.i("FCM token refreshed: %s", token)
        // TODO: send token to backend via secure channel.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title ?: getString(R.string.notification_channel_general)
        val body = remoteMessage.notification?.body ?: ""
        notificationHelper.createChannels()
        notificationHelper.showNotification(title, body)
    }
}
