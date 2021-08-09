package com.ps.omarmattr.aboheshamclient.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.other.COLLECTION_USER
import com.ps.omarmattr.aboheshamclient.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val loginLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    fun login(user:User){
        loginLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_USER).add(user).addOnSuccessListener {
            loginLiveData.postValue(Result.success(""))

        }.addOnFailureListener {
            loginLiveData.postValue(Result.error(it.message, ""))


        }
    }

    val getLoginLiveData get() = loginLiveData

}