package com.ps.omarmattr.aboheshamclient.network

import com.ps.omarmattr.aboheshamclient.model.NotificationParent
import com.ps.omarmattr.aboheshamclient.other.AUTH_VALUE
import com.ps.omarmattr.aboheshamclient.other.VALUE_TYPE
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