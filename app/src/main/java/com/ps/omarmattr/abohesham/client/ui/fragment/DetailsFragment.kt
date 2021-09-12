package com.ps.omarmattr.abohesham.client.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ps.omarmattr.abohesham.client.BR
import com.ps.omarmattr.abohesham.client.adapter.GenericAdapter
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.model.Ticket
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.*
import com.ps.omarmattr.abohesham.client.ui.viewmodel.DetailsViewModel
import com.ps.omarmattr.abohesham.client.util.Result
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.databinding.ProductDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<String> {
    private val mBinding by lazy {
        ProductDetailsFragmentBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModel: DetailsViewModel
    lateinit var product: Product
    lateinit var user: User
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
            mBinding.toolbarLayout, getString(R.string.product_details), false,
            hasMenu = false,
            menuIcon = 0
        ) {
            val a = arguments?.getBoolean("main")
            if (a != null && a) {
                findNavController().navigate(R.id.goToHome)
            } else {
                findNavController().popBackStack()


            }
        }

        requireActivity().getSharedPreferences(
            getString(R.string.app_name_en),
            Context.MODE_PRIVATE
        ).getString(USER_TYPE, null)?.let {
            user = Gson().fromJson(it, User::class.java)


        }

        mBinding.btnAdd.setOnClickListener {
            var count = mBinding.txtQuantity.text.toString().toInt()
            count++
            mBinding.txtQuantity.text = count.toString()
        }
        mBinding.btnRemove.setOnClickListener {
            var count = mBinding.txtQuantity.text.toString().toInt()
            if (count > 0) count--
            mBinding.txtQuantity.text = count.toString()
        }


        mBinding.imageLove.setOnClickListener {
            val isLike = if (product.like.contains(user.id)) {
                product.like.remove(user.id)
                false
            } else {
                product.like.add(user.id)
                true
            }
            viewModel.eventProductLove(product, isLike)
            viewModel.addToLike(product.id, product.like)
            viewModel.getLikeLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Result.Status.EMPTY -> {
                    }
                    Result.Status.ERROR -> {
                    }
                    Result.Status.LOADING -> {

                    }
                    Result.Status.SUCCESS -> {
                        if (isLike)
                            mBinding.imageLove.setImageResource(R.drawable.ic_heart_red) else mBinding.imageLove.setImageResource(
                            R.drawable.ic_heart
                        )

                    }
                }
            }
        }
        requireArguments().getParcelable<Product>(PRODUCT_TYPE)?.let { product ->
            this.product = product
            mBinding.product = product
            if (product.like.contains(user.id)) {
                mBinding.imageLove.setImageResource(R.drawable.ic_heart_red)
            }
            mBinding.listMealImage.apply {
                imageAdapter.data = product.image
                adapter = imageAdapter
                mBinding.dotsIndicator.setViewPager2(this)
            }
            mBinding.btnDetails.setOnClickListener {
                if (mBinding.txtQuantity.text == "0") {
                    Toast.makeText(requireContext(), "الرجاء تحديد كمية", Toast.LENGTH_SHORT).show()
                } else {
                    val ticket = Ticket(
                        id = FirebaseFirestore.getInstance().collection(
                            COLLECTION_TICKET
                        ).document().id,
                        user = user,
                        userId = user.id,
                        quantity = mBinding.txtQuantity.text.toString().toInt(),
                        productId = product.id,
                        product.name, date = Calendar.getInstance().time.time
                    )

                    viewModel.eventProductAddToCard(product)
                    viewModel.addTicket(ticket)
                    findNavController().popBackStack()

                }
                //  DetailsDialog().show(childFragmentManager,"")
            }
        }
    }

    override fun onClickItem(itemViewModel: String, type: Int, position: Int) {
    }

    override fun onClickDelete(itemViewModel: String) {

    }

}