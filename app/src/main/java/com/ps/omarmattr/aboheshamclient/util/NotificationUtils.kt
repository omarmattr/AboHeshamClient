package com.ps.omarmattr.aboheshamclient.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.ps.omarmattr.aboheshamclient.R
import com.ps.omarmattr.aboheshamclient.model.Product
import com.ps.omarmattr.aboheshamclient.other.PRODUCT_TYPE
import com.ps.omarmattr.aboheshamclient.ui.MainActivity

object NotificationUtils {

    private val MAIN_CHANNEL_ID = "main_channel_id"


     fun sendNotification(
        context: Context,
        title: String,
        product: Product
    ) {
        createNotificationChannel(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 putExtra(PRODUCT_TYPE, product)
        }
         val description = " تم اضافة المنتج (${product.name}) اضغط لرؤية التفاصيل"
        val pendingIntent =
            PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
        val builder = NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true).setContentIntent(pendingIntent)


        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MAIN_CHANNEL_ID, MAIN_CHANNEL_ID, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}