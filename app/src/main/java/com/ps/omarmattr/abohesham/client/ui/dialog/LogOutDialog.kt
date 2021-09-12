package com.ps.omarmattr.abohesham.client.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.ps.omarmattr.abohesham.client.databinding.DialogLogoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogOutDialog : DialogFragment() {

    private lateinit var mBinding: DialogLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogLogoutBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        isCancelable = false
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnExit.setOnClickListener {
            requireActivity().finish()
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