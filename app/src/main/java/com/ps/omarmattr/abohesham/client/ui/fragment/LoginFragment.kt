package com.ps.omarmattr.abohesham.client.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.COLLECTION_USER
import com.ps.omarmattr.abohesham.client.other.LOGIN_TYPE
import com.ps.omarmattr.abohesham.client.other.USER_TYPE
import com.ps.omarmattr.abohesham.client.ui.dialog.LoadingDialog
import com.ps.omarmattr.abohesham.client.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"
    private val mBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null
//    private lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        FacebookSdk.sdkInitialize(requireContext())
//        callbackManager = CallbackManager.Factory.create()
        mBinding.btnSignUp.setOnClickListener {
            signUp()
        }

        if (FirebaseAuth.getInstance().currentUser?.uid != null) {
            Log.e("ttttttttttUID", FirebaseAuth.getInstance().currentUser?.uid.toString())
        }

        mBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mBinding.txtNameNet.isVisible = true
                mBinding.textView3.isVisible = true
            } else {
                mBinding.txtNameNet.isVisible = false
                mBinding.textView3.isVisible = false
            }
        }


        callbackManager = CallbackManager.Factory.create();
        mBinding.loginButton.setOnClickListener {
            FacebookSdk.addLoggingBehavior(com.facebook.LoggingBehavior.REQUESTS);
            com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(
                this, java.util.Arrays.asList("public_profile")
            );

            com.facebook.login.LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        // App code
                        val batch = GraphRequestBatch(GraphRequest.newMeRequest(
                            loginResult!!.accessToken
                        ) { jsonObject, response ->
//                                Log.e("ttttttttthandleFaCallBack", jsonObject.toString())

                            handleFacebookAccessToken(loginResult.accessToken) {
                                Log.e("ttttttttthandleFaCallBack", jsonObject.toString())
                            }
                        },
                            GraphRequest.newMyFriendsRequest(
                                loginResult.accessToken
                            ) { jsonArray, response ->

                                Log.e("ttttttttttttt", jsonArray.toString())
                            }
                        )

                        batch.executeAsync()
                    }

                    override fun onCancel() = Unit

                    override fun onError(exception: FacebookException) = Unit
                })
        }
    }

    private fun signUp() {
        loadingDialog = LoadingDialog()
        val name = mBinding.txtName
        val phone = mBinding.txtPhone
        val networkName = mBinding.txtNameNet
        when {
            name.editText?.text!!.isEmpty() -> {
                onChickData(
                    name.editText!!,
                    requireActivity().getString(R.string.fieldRequired)
                )
                return
            }
            phone.editText?.text!!.isEmpty() -> {
                onChickData(
                    phone.editText!!,
                    requireActivity().getString(R.string.fieldRequired)
                )
                return
            }
            phone.editText!!.text.length < 10 -> {
                onChickData(
                    phone.editText!!,
                    requireActivity().getString(R.string.phone_error)
                )
                return
            }
            mBinding.checkBox.isChecked && networkName.editText!!.text.isEmpty() -> {
                onChickData(
                    networkName.editText!!,
                    requireActivity().getString(R.string.fieldRequired)
                )
                return
            }

            else -> {
                val user = User(
                    name = name.editText!!.text.toString(),
                    phone = phone.editText!!.text.toString(),
                    id = FirebaseFirestore.getInstance().collection(
                        COLLECTION_USER
                    ).document().id,
                    isNetwork = mBinding.checkBox.isChecked,
                    networkName = mBinding.txtNameNet.editText!!.text.toString()
                )

//                viewModel.login(
//                    user
////                )
//                mBinding.loginButton.setReadPermissions("email")
//                mBinding.loginButton.registerCallback(
//                    callbackManager,
//                    object : FacebookCallback<LoginResult> {
//                        override fun onSuccess(loginResult: LoginResult) {
//                            Log.e(TAG, "facebook:onSuccess:$loginResult")
//                           // viewModel.handleFacebookAccessToken(loginResult.accessToken)
//                        }
//
//                        override fun onCancel() {
//                            Log.e(TAG, "facebook:onCancel")
//                        }
//
//                        override fun onError(error: FacebookException) {
//                            Log.e(TAG, "facebook:onError", error)
//                        }
//                    })


                viewModel.getLoginLiveData.observe(viewLifecycleOwner) {
                    when (it.status) {

                        Result.Status.EMPTY -> {
                        }
                        Result.Status.ERROR -> {
                            loadingDialog.dismiss()
                            loading = null
                        }
                        Result.Status.LOADING -> {
                            if (!loadingDialog.isAdded && loading == null)
                                loadingDialog.show(requireActivity().supportFragmentManager, "")
                            loading = it.data.toString()
                        }
                        Result.Status.SUCCESS -> {
                            if (loading != null) {
                                try {
                                    loadingDialog.dismiss()
                                    loading = null

                                } catch (e: Exception) {
                                }
                            }
                            val shared = requireActivity().getSharedPreferences(
                                getString(R.string.app_name_en),
                                Context.MODE_PRIVATE
                            )
                            val editor = shared.edit()
                            editor.putBoolean(LOGIN_TYPE, true)
                            editor.putString(USER_TYPE, Gson().toJson(user))
                            editor.apply()
                            findNavController().navigate(R.id.goToHome)
                        }
                    }
                }
            }


        }
    }


    private var callbackManager: CallbackManager? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("ttttttttttt", "OnAcitivy")
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

    }


    private fun handleFacebookAccessToken(token: AccessToken, onComplete: () -> Unit) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        Log.e("ttttthandleFacebookAccessToken", credential.provider.toString())
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete()
                } else {
                    task.exception?.printStackTrace()
                }

            }
    }

    private fun onChickData(txt: EditText, message: String) {
        txt.error = message
        txt.requestFocus()
    }

}