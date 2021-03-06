package com.ps.omarmattr.abohesham.client.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ps.omarmattr.abohesham.client.adapter.GenericAdapter
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.other.CATEGORY_NAME
import com.ps.omarmattr.abohesham.client.other.PRODUCT_TYPE
import com.ps.omarmattr.abohesham.client.other.setToolbarView
import com.ps.omarmattr.abohesham.client.ui.dialog.LoadingDialog
import com.ps.omarmattr.abohesham.client.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.BR
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.databinding.FragmentCategoryDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryDetailsFragment : Fragment(),GenericAdapter.OnListItemViewClickListener<Product> {
    private val mBinding by lazy {
        FragmentCategoryDetailsBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: HomeViewModel
    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_product, BR.product, this)
    }

    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )= mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog()
        mBinding.rcProduct.adapter = mAdapter
        requireArguments().getString(CATEGORY_NAME)?.let {
            viewModel.getAllProductWhere(it)
            requireActivity().setToolbarView(
                mBinding.toolbarView, it, false,
                hasMenu = false,
                menuIcon = 0
            ) {
                findNavController().popBackStack()
            }
        }
        viewModel.getProductWhereGetLiveData.observe(viewLifecycleOwner){
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

   findNavController().navigate(R.id.goToDetails,bundle)
    }

    override fun onClickDelete(itemViewModel: Product) {

    }


}