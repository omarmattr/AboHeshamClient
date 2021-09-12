package com.ps.omarmattr.abohesham.client.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ps.omarmattr.abohesham.client.BR
import com.ps.omarmattr.abohesham.client.ui.dialog.LoadingDialog
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.adapter.GenericAdapter
import com.ps.omarmattr.abohesham.client.adapter.RadioAdapter
import com.ps.omarmattr.abohesham.client.model.Category
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.other.PRODUCT_TYPE
import com.ps.omarmattr.abohesham.client.other.TWO
import com.ps.omarmattr.abohesham.client.other.setToolbarView
import com.ps.omarmattr.abohesham.client.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<Product> , RadioAdapter.OnClick{
    private val mBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: HomeViewModel

    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_product, BR.product, this)
    }
    private val mCAdapter by lazy {
        RadioAdapter(requireActivity(), arrayListOf(),this)
    }

        private lateinit var allProduct: ArrayList<Product>
    private lateinit var loadingDialog: LoadingDialog
    private var loading: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog()
        viewModel.getAllProduct()
        requireActivity().setToolbarView(
            mBinding.toolbarLayout, getString(R.string.home), true,
            hasMenu = true,
            menuIcon = 0
        ) {
            if (it == TWO){
                findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)

            }else{

            }


        }
        mBinding.searchView.editText!!.doAfterTextChanged { t ->
            val array = allProduct.filter { it.name.contains(t.toString()) }
            mAdapter.data = array
            viewModel.eventSearchProduct("search",t.toString())
        }
        mBinding.rcCategory.adapter = mCAdapter
        viewModel.getAllCategory()
        viewModel.getCategoryGetLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Result.Status.EMPTY -> {
                }
                Result.Status.ERROR -> {
                }
                Result.Status.LOADING -> {

                }
                Result.Status.SUCCESS -> {
                    val array = it.data as ArrayList<Category>
                    array.add(0, Category("all","جميع الاصناف"))
                        mCAdapter.data = array
                    mCAdapter.notifyDataSetChanged()



                }
            }
        }
        mBinding.rcProduct.adapter = mAdapter
        viewModel.getProductGetLiveData.observe(viewLifecycleOwner) {
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
                        allProduct = it.data as ArrayList<Product>
                        mAdapter.data = allProduct

                    }
                }
            }
        }


    }

    override fun onClickItem(itemViewModel: Product, type: Int, position: Int) {
        val bundle = Bundle().apply {
            putParcelable(PRODUCT_TYPE, itemViewModel)
        }
        viewModel.eventClickProduct(itemViewModel)
        findNavController().navigate(R.id.goToDetails,bundle)


    }

    override fun onClick(category: Category) {
        val array =   if (category.id == "all") allProduct else
         allProduct.filter { it.category.contains(category.name) }
        mAdapter.data = array
        viewModel.eventSearchProduct(category.name,category.name)
    }

    override fun onClickDelete(itemViewModel: Product) {

    }


}