package com.ps.omarmattr.aboheshamclient.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.ps.omarmattr.aboheshamclient.model.Product
import com.ps.omarmattr.aboheshamclient.model.Ticket
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.repository.DetailsRepository
import com.ps.omarmattr.aboheshamclient.repository.HomeRepository
import com.ps.omarmattr.aboheshamclient.repository.LoginRepository
import com.ps.omarmattr.aboheshamclient.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    application: Application,
    private val repository: DetailsRepository
) : AndroidViewModel(application) {
    fun addTicket(ticket: Ticket)= repository.addTicket(ticket)
    val getTicketLiveData get() =  repository.getTicketLiveData

}