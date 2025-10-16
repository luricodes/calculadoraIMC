package com.comunidadedevspace.imc.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.comunidadedevspace.imc.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper
    @Inject
    constructor(
        private val context: Context,
    ) {
        fun createChannels() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        CHANNEL_GENERAL,
                        context.getString(R.string.notification_channel_general),
                        NotificationManager.IMPORTANCE_DEFAULT,
                    )
                NotificationManagerCompat.from(context).createNotificationChannel(channel)
            }
        }

        fun showNotification(
            title: String,
            message: String,
        ) {
            val builder =
                NotificationCompat.Builder(context, CHANNEL_GENERAL)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }

        companion object {
            const val CHANNEL_GENERAL = "general"
            private const val NOTIFICATION_ID = 42
        }
    }
