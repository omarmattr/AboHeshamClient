package com.ps.omarmattr.abohesham.client.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.repository.HomeRepository
import com.ps.omarmattr.abohesham.client.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: LoginRepository
) : AndroidViewModel(application) {
    fun login(user: User)= repository.login(user)
    fun handleFacebookAccessToken(token: AccessToken) = repository.handleFacebookAccessToken(token)
    val getLoginLiveData get() =  repository.getLoginLiveData

}