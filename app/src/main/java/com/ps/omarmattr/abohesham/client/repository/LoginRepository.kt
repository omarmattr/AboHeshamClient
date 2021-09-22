package com.ps.omarmattr.abohesham.client.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.COLLECTION_USER
import com.ps.omarmattr.abohesham.client.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val loginLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    fun login(user: User) {
        loginLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_USER).whereEqualTo("phone", user.phone)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    if (value!!.documents.isNotEmpty()) {
                        user.id = value.documents.first().id
                        db.collection(COLLECTION_USER).document(user.id).set(user).addOnSuccessListener {
                            loginLiveData.postValue(Result.success(""))

                        }.addOnFailureListener {
                            loginLiveData.postValue(Result.error(it.message, ""))


                        }
                    } else {
                        db.collection(COLLECTION_USER).document(user.id).set(user).addOnSuccessListener {
                            loginLiveData.postValue(Result.success(""))

                        }.addOnFailureListener {
                            loginLiveData.postValue(Result.error(it.message, ""))


                        }
                    }
                }
            }

    }

    val getLoginLiveData get() = loginLiveData

}