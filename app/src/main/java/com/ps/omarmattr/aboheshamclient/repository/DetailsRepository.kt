package com.ps.omarmattr.aboheshamclient.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.aboheshamclient.model.NotificationParent
import com.ps.omarmattr.aboheshamclient.model.Ticket
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.other.COLLECTION_TICKET
import com.ps.omarmattr.aboheshamclient.other.COLLECTION_USER
import com.ps.omarmattr.aboheshamclient.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DetailsRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val ticketLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    @Inject
    lateinit var notificationInterface: NotificationRepository

    fun addTicket(ticket: Ticket){
        ticketLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_TICKET).add(ticket).addOnSuccessListener {
            notificationInterface.sendRemoteMessage(
                NotificationParent(
                    data = ticket,
                    to = "/topics/admin"
                ),
            )
            ticketLiveData.postValue(Result.success(""))

        }.addOnFailureListener {
            ticketLiveData.postValue(Result.error(it.message, ""))


        }
    }

    val getTicketLiveData get() = ticketLiveData

}