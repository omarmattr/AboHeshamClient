package com.ps.omarmattr.abohesham.client.network

import com.ps.omarmattr.abohesham.client.model.NotificationParent
import com.ps.omarmattr.abohesham.client.other.AUTH_VALUE
import com.ps.omarmattr.abohesham.client.other.VALUE_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {

    @Headers(
        "Authorization: key=${AUTH_VALUE}",
        "Content-Type:${VALUE_TYPE}"
    )
    @POST("send")

    suspend fun sendRemoteMessage(
        @Body notification: NotificationParent
    ): Response<ResponseBody>

}