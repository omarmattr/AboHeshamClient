package com.ps.omarmattr.aboheshamclient.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ps.omarmattr.aboheshamclient.R
import com.ps.omarmattr.aboheshamclient.databinding.FragmentTicketBinding
import com.ps.omarmattr.aboheshamclient.model.Ticket
import com.ps.omarmattr.aboheshamclient.model.Tickets
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.other.USER_TYPE
import com.ps.omarmattr.aboheshamclient.ui.dialog.LoadingDialog
import com.ps.omarmattr.aboheshamclient.ui.viewmodel.TicketViewModel
import com.ps.omarmattr.aboheshamclient.util.Result
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TicketFragment : Fragment() {
    private val mBinding by lazy {
        FragmentTicketBinding.inflate(layoutInflater)
    }
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null

    @Inject
    lateinit var viewModel: TicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().getSharedPreferences(
            getString(R.string.app_name_en),
            Context.MODE_PRIVATE
        ).getString(USER_TYPE, null)?.let {
            val user = Gson().fromJson(it, User::class.java)
            loadingDialog = LoadingDialog()

            viewModel.getMyTicket(user.id)

        }

        viewModel.getTicketLiveData.observe(viewLifecycleOwner) {
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
                        val data  = it.data as ArrayList<Ticket>

                        Log.e("pppp", data.toString())

                    }
                }
            }
        }
    }
}