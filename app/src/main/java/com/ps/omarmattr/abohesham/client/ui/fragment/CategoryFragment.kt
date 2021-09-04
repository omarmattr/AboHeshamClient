package com.ps.omarmattr.abohesham.client.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ps.omarmattr.abohesham.client.adapter.GenericAdapter
import com.ps.omarmattr.abohesham.client.model.Category
import com.ps.omarmattr.abohesham.client.other.CATEGORY_NAME
import com.ps.omarmattr.abohesham.client.other.setToolbarView
import com.ps.omarmattr.abohesham.client.ui.dialog.LoadingDialog
import com.ps.omarmattr.abohesham.client.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.aboheshamclient.BR
import com.ps.omarmattr.aboheshamclient.R
import com.ps.omarmattr.aboheshamclient.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment() ,GenericAdapter.OnListItemViewClickListener<Category>{
    private val mBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: HomeViewModel
    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_category, BR.category, this)
    }

    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog()
        requireActivity().setToolbarView(
            mBinding.toolbarLayout, getString(R.string.category), false,
            hasMenu = false,
            menuIcon = 0
        ) {
            requireActivity().onBackPressed()
        }
        mBinding.rcCategory.adapter = mAdapter
        viewModel.getAllCategory()
        viewModel.getCategoryGetLiveData.observe(viewLifecycleOwner){
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
                        mAdapter.data = it.data as ArrayList<Category>

                    }
                }
            }
        }
    }

    override fun onClickItem(itemViewModel: Category, type: Int, position: Int) {
        val bundle = Bundle().apply {
            putString(CATEGORY_NAME, itemViewModel.name)
        }
        findNavController().navigate(R.id.action_categoryFragment_to_categoryDetailsFragment,bundle)

    }

    override fun onClickDelete(itemViewModel: Category) {

    }


}