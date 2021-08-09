package com.ps.omarmattr.aboheshamclient.ui.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.ps.omarmattr.aboheshamclient.R
import com.ps.omarmattr.aboheshamclient.databinding.FragmentSplashBinding
import com.ps.omarmattr.aboheshamclient.model.Product
import com.ps.omarmattr.aboheshamclient.model.Version
import com.ps.omarmattr.aboheshamclient.other.LOGIN_TYPE
import com.ps.omarmattr.aboheshamclient.other.PRODUCT_TYPE
import com.ps.omarmattr.aboheshamclient.ui.dialog.LogOutDialog
import com.ps.omarmattr.aboheshamclient.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.aboheshamclient.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val mBinding by lazy {
        FragmentSplashBinding.inflate(layoutInflater)
    }

    private var product: Product? = null

    @Inject
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product = arguments?.getParcelable(PRODUCT_TYPE)
        CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getVersion()
                viewModel.getVersionLiveData.observe(viewLifecycleOwner) {
                    when (it.status) {

                        Result.Status.SUCCESS -> {

                            try {
                                val pInfo = requireContext().packageManager.getPackageInfo(
                                    requireContext().packageName,
                                    0
                                )
                                viewModel.eventLogin(pInfo.versionName)
                                val version = pInfo.versionName
                                if (version == (it.data as Version).name) {
                                    val sheard = requireActivity().getSharedPreferences(getString(R.string.app_name_en),
                                        Context.MODE_PRIVATE)
                                    if (sheard.getBoolean(LOGIN_TYPE,false)){

                                        if (product == null) {
                                            findNavController().navigate(R.id.goToHome)
                                        } else {
                                            val bundle = Bundle().apply {
                                                putParcelable(PRODUCT_TYPE, product)
                                                putBoolean("main", true)
                                            }
                                            findNavController().navigate(R.id.goToDetails,bundle)
                                        }
                                    }else{

                                  findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                                    }

                                } else {
                                    val f = LogOutDialog()
                                    f.show(childFragmentManager, "")

                                }
                            } catch (e: PackageManager.NameNotFoundException) {
                                e.printStackTrace()
                            }


                        }
                        else ->{}
                    }
                }
            }
        }
    }
}
