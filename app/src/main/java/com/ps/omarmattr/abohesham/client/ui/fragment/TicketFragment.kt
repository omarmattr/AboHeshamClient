package com.ps.omarmattr.abohesham.client.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ps.omarmattr.abohesham.client.BR
import com.ps.omarmattr.abohesham.client.adapter.GenericAdapter
import com.ps.omarmattr.abohesham.client.model.Ticket
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.*
import com.ps.omarmattr.abohesham.client.ui.dialog.LoadingDialog
import com.ps.omarmattr.abohesham.client.ui.dialog.TicketDialog
import com.ps.omarmattr.abohesham.client.ui.viewmodel.TicketViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.databinding.FragmentTicketBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TicketFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<Ticket> {
    private val mBinding by lazy {
        FragmentTicketBinding.inflate(layoutInflater)
    }
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null

    @Inject
    lateinit var viewModel: TicketViewModel

    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_ticket, BR.ticket, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setToolbarView(
            mBinding.toolbarLayout, getString(R.string.ticket), true,
            hasMenu = false,
            menuIcon = 0
        ) {


        }
        requireActivity().getSharedPreferences(
            getString(R.string.app_name_en),
            Context.MODE_PRIVATE
        ).getString(USER_TYPE, null)?.let {
            val user = Gson().fromJson(it, User::class.java)
            loadingDialog = LoadingDialog()

            viewModel.getMyTicket(user.id)

        }
        mBinding.rcTicket.adapter = mAdapter

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
                        val data = it.data as ArrayList<Ticket>
                        mAdapter.data = data

                        Log.e("pppp", data.toString())

                    }
                }
            }
        }
    }

    override fun onClickItem(itemViewModel: Ticket, type: Int, position: Int) {

    }

    override fun onClickDelete(itemViewModel: Ticket) {
        val bundle = Bundle().apply {
            putString(PRODUCT_ID, itemViewModel.id)
        }
        val f = TicketDialog()
        f.arguments = bundle
        f.show(childFragmentManager, "")
    }
}