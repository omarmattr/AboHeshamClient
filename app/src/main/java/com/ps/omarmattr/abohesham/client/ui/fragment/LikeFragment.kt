package com.ps.omarmattr.abohesham.client.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ps.omarmattr.abohesham.client.adapter.GenericAdapter
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.PRODUCT_TYPE
import com.ps.omarmattr.abohesham.client.other.USER_TYPE
import com.ps.omarmattr.abohesham.client.other.setToolbarView
import com.ps.omarmattr.abohesham.client.ui.dialog.LoadingDialog
import com.ps.omarmattr.abohesham.client.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.abohesham.client.ui.viewmodel.LikeViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.BR
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.databinding.FragmentLikeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LikeFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<Product> {
    private val mBinding by lazy {
        FragmentLikeBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: LikeViewModel


    @Inject
    lateinit var homeViewModel: HomeViewModel


    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_product, BR.product, this)
    }
    lateinit var user: User
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog()
        requireActivity().getSharedPreferences(
            getString(R.string.app_name_en),
            Context.MODE_PRIVATE
        ).getString(USER_TYPE, null)?.let {
            user = Gson().fromJson(it, User::class.java)


        }
        mBinding.rcProduct.adapter = mAdapter
        viewModel.getUserLike(user.id)
        requireActivity().setToolbarView(
            mBinding.toolbarLayout, getString(R.string.like), true,
            hasMenu = false,
            menuIcon = 0
        ) {
        }
        viewModel.getLikeLiveData.observe(viewLifecycleOwner) {
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
                        mAdapter.data = it.data as ArrayList<Product>

                    }
                }
            }
        }
    }

    override fun onClickItem(itemViewModel: Product, type: Int, position: Int) {
        val bundle = Bundle().apply {
            putParcelable(PRODUCT_TYPE, itemViewModel)
        }
        homeViewModel.eventClickProduct(itemViewModel)
        findNavController().navigate(R.id.goToDetails, bundle)

    }

    override fun onClickDelete(itemViewModel: Product) {
    }
}