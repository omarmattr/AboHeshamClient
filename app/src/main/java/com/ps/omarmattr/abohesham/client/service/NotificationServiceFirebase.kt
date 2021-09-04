package com.ps.omarmattr.abohesham.client.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.ps.omarmattr.abohesham.client.model.NotificationData
import com.ps.omarmattr.abohesham.client.util.NotificationUtils

class NotificationServiceFirebase : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM", "Token: $token")
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data.toString()
        Log.e("tttttttttt", data)
        val product =  Gson().fromJson(data,NotificationData::class.java).data
        NotificationUtils.sendNotification(
            this,
            product.name,
          product)
    }


}