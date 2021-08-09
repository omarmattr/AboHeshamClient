package com.ps.omarmattr.aboheshamclient.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.aboheshamclient.model.NotificationParent
import com.ps.omarmattr.aboheshamclient.model.Ticket
import com.ps.omarmattr.aboheshamclient.model.Tickets
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.other.COLLECTION_TICKET
import com.ps.omarmattr.aboheshamclient.other.COLLECTION_USER
import com.ps.omarmattr.aboheshamclient.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TicketRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val ticketLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    fun getMyTicket(id:String){
        ticketLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_TICKET).whereEqualTo("userId",id).addSnapshotListener { value, error ->
            if (error == null){
                val arrayList = arrayListOf<Ticket>()
                for (i in value!!.documents){
                    arrayList.add(i.toObject(Ticket::class.java)!!)
                }
                ticketLiveData.postValue(Result.success(arrayList))

            }else{
                ticketLiveData.postValue(Result.error(error.message, ""))

            }
        }
    }

    val getTicketLiveData get() = ticketLiveData

}