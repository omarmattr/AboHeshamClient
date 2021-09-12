package com.ps.omarmattr.abohesham.client.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.ps.omarmattr.abohesham.client.other.PRODUCT_ID
import com.ps.omarmattr.abohesham.client.ui.viewmodel.TicketViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.databinding.DialogTicketBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TicketDialog : DialogFragment() {

    private lateinit var mBinding: DialogTicketBinding

    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null
    @Inject
    lateinit var viewModel: TicketViewModel

    private var id:String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogTicketBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        isCancelable = false
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireArguments().getString(PRODUCT_ID, null)?.let {

            id =  it
        }
        mBinding.btnDelete.setOnClickListener {
            loadingDialog = LoadingDialog()
            if (id!=null)viewModel.deleteTicket(id!!)
            viewModel.getDeleteLiveData.observe(viewLifecycleOwner) {
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
                            dismiss()

                        }
                    }
                }
            }
        }
        mBinding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}