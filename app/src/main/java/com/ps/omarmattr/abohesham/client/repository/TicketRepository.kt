package com.ps.omarmattr.abohesham.client.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.abohesham.client.model.Ticket
import com.ps.omarmattr.abohesham.client.other.COLLECTION_TICKET
import com.ps.omarmattr.abohesham.client.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TicketRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val ticketLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val deleteLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

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
    fun deleteTicket(id:String){
        deleteLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_TICKET).document(id).delete().addOnSuccessListener {

            deleteLiveData.postValue(Result.success(""))

        }.addOnFailureListener {
            deleteLiveData.postValue(Result.error(it.message, ""))


        }
    }

    val getTicketLiveData get() = ticketLiveData
    val getDeleteLiveData get() = deleteLiveData

}