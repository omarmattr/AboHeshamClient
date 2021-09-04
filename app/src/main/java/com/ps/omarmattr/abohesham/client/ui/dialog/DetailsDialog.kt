package com.ps.omarmattr.abohesham.client.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.ps.omarmattr.abohesham.client.model.Profile
import com.ps.omarmattr.abohesham.client.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.aboheshamclient.databinding.DialogDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsDialog : DialogFragment() {
    private val mBinding by lazy {
        DialogDetailsBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: HomeViewModel
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog()

        mBinding.btnCancel.setOnClickListener {
            dismiss()
        }
        mBinding.btnDone.setOnClickListener {
            dismiss()
        }
        viewModel.getProfile()
        viewModel.profileGetLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Result.Status.EMPTY -> {
                }
                Result.Status.ERROR -> {
                    loadingDialog.dismiss()
                }
                Result.Status.LOADING -> {
                    loading = it.data.toString()
                    if (!loadingDialog.isAdded)
                        loadingDialog.show(requireActivity().supportFragmentManager, "")
                }
                Result.Status.SUCCESS -> {
                    if (loading != null) {
                        try {
                            loadingDialog.dismiss()
                        } catch (e: Exception) {
                        }
                        mBinding.profile = it.data as Profile

                    }
                }
            }
        }

    }

}