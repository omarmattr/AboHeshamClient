package com.ps.omarmattr.abohesham.client.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val loginLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    fun login(user: User) {
        loginLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_USER).whereEqualTo("phone", user.phone)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    if (value!!.documents.isNotEmpty()) {
                        user.id = value.documents.first().id
                        db.collection(COLLECTION_USER).document(user.id).set(user)
                            .addOnSuccessListener {
                                loginLiveData.postValue(Result.success(""))

                            }.addOnFailureListener {
                            loginLiveData.postValue(Result.error(it.message, ""))


                        }
                    } else {
                        db.collection(COLLECTION_USER).document(user.id).set(user)
                            .addOnSuccessListener {
                                loginLiveData.postValue(Result.success(""))

                            }.addOnFailureListener {
                            loginLiveData.postValue(Result.error(it.message, ""))


                        }
                    }
                }
            }

    }

    fun facLogin(buttonFacebookLogin: LoginButton,callbackManager :CallbackManager ) {
        // Initialize Facebook Login button

        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("TAG", "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("TAG", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG", "facebook:onError", error)
                }
            })
        // ...

    }
     fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                   // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)

                  //  updateUI(null)
                }
            }
    }
    val getLoginLiveData get() = loginLiveData

}