package com.ps.omarmattr.abohesham.client.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.abohesham.client.model.Data
import com.ps.omarmattr.abohesham.client.model.NotificationParent
import com.ps.omarmattr.abohesham.client.model.Ticket
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.COLLECTION_PRODUCT
import com.ps.omarmattr.abohesham.client.other.COLLECTION_TICKET
import com.ps.omarmattr.abohesham.client.other.COLLECTION_USER
import com.ps.omarmattr.abohesham.client.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DetailsRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val ticketLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val likeLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    @Inject
    lateinit var notificationInterface: NotificationRepository

    fun addTicket(ticket: Ticket) {
        Log.e("wwwwwwww12",ticket.toString())
        ticketLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_TICKET).document(ticket.id).set(ticket).addOnSuccessListener {
            notificationInterface.sendRemoteMessage(
                NotificationParent(
                    data = Data(ticket),
                    to = "/topics/admin"
                ),
            )
            Log.e("wwwwwwww1","addOnSuccessListener")

            ticketLiveData.postValue(Result.success(""))

        }.addOnFailureListener {
            Log.e("wwwwwwww",it.message.toString())
            ticketLiveData.postValue(Result.error(it.message, ""))


        }.addOnCanceledListener {
            Log.e("addOnCanceledListener","iiiiiiiiii")

        }
    }

    fun addToLike(id: String, likes: List<String>) {
        likeLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_PRODUCT).document(id).update("like", likes).addOnSuccessListener {
            likeLiveData.postValue(Result.success(""))

        }.addOnFailureListener {
            likeLiveData.postValue(Result.error(it.message, ""))


        }
    }

    val getTicketLiveData get() = ticketLiveData
    val getLikeLiveData get() = likeLiveData

}