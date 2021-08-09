package com.ps.omarmattr.aboheshamclient.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.ps.omarmattr.aboheshamclient.model.Product
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.repository.HomeRepository
import com.ps.omarmattr.aboheshamclient.repository.LoginRepository
import com.ps.omarmattr.aboheshamclient.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    application: Application,
    private val repository: TicketRepository
) : AndroidViewModel(application) {
    fun getMyTicket(id: String)= repository.getMyTicket(id)
    val getTicketLiveData get() =  repository.getTicketLiveData

}