package com.ps.omarmattr.abohesham.client.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: HomeRepository
) : AndroidViewModel(application) {
    private val firebaseAnalytics by lazy {
        Firebase.analytics

    }


    fun getAllProduct() = repository.getAllProduct()
    val getProductGetLiveData get() = repository.getProductGetLiveData
    fun getAllProductWhere(category: String) = repository.getAllProductWhere(category)
    val getProductWhereGetLiveData get() = repository.getProductWhereGetLiveData

    fun delete(productId: String) = repository.delete(productId)
    val deleteProductGetLiveData get() = repository.deleteProductGetLiveData
    fun getProfile() = repository.getProfile()
    val profileGetLiveData get() = repository.profileGetLiveData

    fun eventLogin(version: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param("version", version)
        }
    }

    fun eventClickProduct(product: Product) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_NAME, product.name)
            param(FirebaseAnalytics.Param.ITEM_ID, product.id)
            param(FirebaseAnalytics.Param.ITEM_CATEGORY, product.category)
        }
    }

    fun eventProductAddToCard(product: Product, delete: Boolean = false) {
        val name =
            if (delete) FirebaseAnalytics.Event.REMOVE_FROM_CART else FirebaseAnalytics.Event.ADD_TO_CART
        firebaseAnalytics.logEvent(name) {
            param(FirebaseAnalytics.Param.ITEM_NAME, product.name)
            param(FirebaseAnalytics.Param.ITEM_ID, product.id)
            param(FirebaseAnalytics.Param.ITEM_CATEGORY, product.category)
        }
    }

    fun eventProductLove(product: Product, like: Boolean = false) {
        val name = if (like) "Remove_to_favourite" else "Add_to_favourite"
        firebaseAnalytics.logEvent(name) {

            param(FirebaseAnalytics.Param.ITEM_NAME, product.name)
            param(FirebaseAnalytics.Param.ITEM_ID, product.id)
            param(FirebaseAnalytics.Param.ITEM_CATEGORY, product.category)
        }
    }

    fun eventSearchProduct(id: String, search: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH) {
            param(FirebaseAnalytics.Param.ITEM_CATEGORY, id)
            param(FirebaseAnalytics.Param.SEARCH_TERM, search)
        }
    }

    fun getAllCategory() = repository.getAllCategory()
    val getCategoryGetLiveData get() = repository.getCategoryGetLiveData
    fun getVersion() = repository.getVersion()
    val getVersionLiveData get() = repository.getVersionLiveData
}