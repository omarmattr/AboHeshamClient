package com.ps.omarmattr.aboheshamclient.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ps.omarmattr.aboheshamclient.BR
import com.ps.omarmattr.aboheshamclient.R
import com.ps.omarmattr.aboheshamclient.adapter.GenericAdapter
import com.ps.omarmattr.aboheshamclient.databinding.ProductDetailsFragmentBinding
import com.ps.omarmattr.aboheshamclient.model.Product
import com.ps.omarmattr.aboheshamclient.model.Ticket
import com.ps.omarmattr.aboheshamclient.model.User
import com.ps.omarmattr.aboheshamclient.other.LOGIN_TYPE
import com.ps.omarmattr.aboheshamclient.other.PRODUCT_TYPE
import com.ps.omarmattr.aboheshamclient.other.USER_TYPE
import com.ps.omarmattr.aboheshamclient.other.setToolbarView
import com.ps.omarmattr.aboheshamclient.ui.dialog.DetailsDialog
import com.ps.omarmattr.aboheshamclient.ui.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<String> {
    private val mBinding by lazy {
        ProductDetailsFragmentBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel:DetailsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    private val imageAdapter by lazy {
        GenericAdapter(R.layout.item_meal_image, BR.image, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setToolbarView(
            mBinding.toolbar, getString(R.string.product_details), false,
            hasMenu = false,
            menuIcon = 0
        ) {
            val a = arguments?.getBoolean("main")
            if (a != null && a) {
                findNavController().navigate(R.id.goToHome)
            } else {
                requireActivity().onBackPressed()

            }
        }

        mBinding.btnAdd.setOnClickListener {
            var count = mBinding.txtQuantity.text.toString().toInt()
            count++
            mBinding.txtQuantity.text = count.toString()
        }
        mBinding.btnRemove.setOnClickListener {
            var count = mBinding.txtQuantity.text.toString().toInt()
            if (count < 0 )count--
            mBinding.txtQuantity.text = count.toString()
        }
        requireArguments().getParcelable<Product>(PRODUCT_TYPE)?.let { product ->
            mBinding.product = product
            mBinding.listMealImage.apply {
                imageAdapter.data = product.image
                adapter = imageAdapter
                mBinding.dotsIndicator.setViewPager2(this)
            }
            mBinding.btnDetails.setOnClickListener {
                if (mBinding.txtQuantity.text == "0") {

                } else {
                    requireActivity().getSharedPreferences(
                        getString(R.string.app_name_en),
                        Context.MODE_PRIVATE
                    ).getString(USER_TYPE, null)?.let {
                        val user = Gson().fromJson(it, User::class.java)
                        val ticket = Ticket(
                            user = user,
                            userId = user.id,
                            quantity = mBinding.txtQuantity.text.toString().toInt(),
                            productId = product.id,
                            product.name
                        )
                        viewModel.addTicket(ticket)

                    }

                }
                //  DetailsDialog().show(childFragmentManager,"")
            }
        }
    }

    override fun onClickItem(itemViewModel: String, type: Int, position: Int) {
    }

}