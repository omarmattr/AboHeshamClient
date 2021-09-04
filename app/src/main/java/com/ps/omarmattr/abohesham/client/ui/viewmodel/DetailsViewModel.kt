package com.ps.omarmattr.abohesham.client.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.model.Ticket
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.repository.DetailsRepository
import com.ps.omarmattr.abohesham.client.repository.HomeRepository
import com.ps.omarmattr.abohesham.client.repository.LoginRepository
import com.ps.omarmattr.abohesham.client.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    application: Application,
    private val repository: DetailsRepository
) : AndroidViewModel(application) {
    private val firebaseAnalytics by lazy {
        Firebase.analytics

    }

    fun addTicket(ticket: Ticket)= repository.addTicket(ticket)
    fun addToLike(id:String,likes:List<String>)= repository.addToLike(id,likes)
    val getTicketLiveData get() =  repository.getTicketLiveData
    val getLikeLiveData get() =  repository.getLikeLiveData

    fun eventProductAddToCard(product: Product) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            param(FirebaseAnalytics.Param.ITEM_NAME, product.name)
            param(FirebaseAnalytics.Param.ITEM_ID, product.id)
            param(FirebaseAnalytics.Param.ITEM_CATEGORY, product.category)
        }
    }

    fun eventProductLove(product: Product, like: Boolean = false) {
        val name = if (like) "Remove_to_favourite" else "Add_to_favourite"
        firebaseAnalytics.logEvent(name) {

            param(FirebaseAnalytics.Param.ITEM_NAME, product.name)
            param(FirebaseAnalytics.Param.ITEM_ID, product.id)
            param(FirebaseAnalytics.Param.ITEM_CATEGORY, product.category)
        }
    }

}