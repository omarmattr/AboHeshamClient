package com.ps.omarmattr.abohesham.client.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private val mBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null

    @Inject
    lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnSignUp.setOnClickListener {
            signUp()
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
            mBinding.checkBox.isChecked -> {
                if (networkName.editText!!.text.isEmpty()) {
                    onChickData(
                        networkName.editText!!,
                        requireActivity().getString(R.string.fieldRequired)
                    )
                    return
                }
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

                viewModel.login(
                    user
                )
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

    private fun onChickData(txt: EditText, message: String) {
        txt.error = message
        txt.requestFocus()
    }

}