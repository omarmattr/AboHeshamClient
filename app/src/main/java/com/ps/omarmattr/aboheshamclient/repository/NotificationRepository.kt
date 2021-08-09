package com.ps.omarmattr.aboheshamclient.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ps.omarmattr.aboheshamclient.model.NotificationParent
import com.ps.omarmattr.aboheshamclient.network.NotificationInterface
import kotlinx.coroutines.withContext
import com.ps.omarmattr.aboheshamclient.util.Result

import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    val notificationInterface: NotificationInterface
) {
    private val notificationMutableLiveData: MutableLiveData<Result<Any>> = MutableLiveData()


    fun sendRemoteMessage(notification: NotificationParent) {
        Log.e("ttttttttttttsdfsdf", notification.data.toString())
        CoroutineScope(Dispatchers.IO).launch {
            notificationMutableLiveData.postValue(Result.loading("loading"))
            val response = notificationInterface.sendRemoteMessage(notification)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            notificationMutableLiveData.postValue(Result.success(it))
                            Log.e("ttttttt21", it.charStream().readText())
                        }

                    } else {
                        Log.e("ttttttt22", response.errorBody()!!.charStream().readText())
                        notificationMutableLiveData.postValue(Result.success("Ooops: ${response.errorBody()}"))
                    }
                } catch (e: HttpException) {
                    Log.e("ttttttt1", e.message().toString())
                    notificationMutableLiveData.postValue(Result.success("Ooops: ${e.message()}"))

                } catch (t: Throwable) {
                    Log.e("ttttttt2", t.message.toString())
                    notificationMutableLiveData.postValue(Result.success("Ooops: ${t.message}"))
                }
            }
        }
    }


    val notificationPostGetLiveData get() = notificationMutableLiveData

}