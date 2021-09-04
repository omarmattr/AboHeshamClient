package com.ps.omarmattr.abohesham.client.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.abohesham.client.model.Category
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.model.Profile
import com.ps.omarmattr.abohesham.client.model.Version
import com.ps.omarmattr.abohesham.client.other.COLLECTION_CATEGORY
import com.ps.omarmattr.abohesham.client.other.COLLECTION_PRODUCT
import com.ps.omarmattr.abohesham.client.other.COLLECTION_PROFILE
import com.ps.omarmattr.abohesham.client.other.COLLECTION_VERSION
import com.ps.omarmattr.abohesham.client.util.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val getProductLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val getProductWhereLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val deleteProductLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val profileLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val getCategoryLiveData: MutableLiveData<Result<Any>> = MutableLiveData()
    private val versionLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    fun getVersion(){
        versionLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_VERSION).document(COLLECTION_VERSION).addSnapshotListener { value, error ->
            if (error == null) {

                versionLiveData.postValue(Result.success(value!!.toObject(Version::class.java)!!))
            } else {
                versionLiveData.postValue(Result.error(error.message, ""))
            }

        }
    }
    fun getAllProduct() {
        getProductLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_PRODUCT).addSnapshotListener { value, error ->
            if (error == null) {
                val array = arrayListOf<Product>()
                value!!.forEach {
                    array.add(it.toObject(Product::class.java))
                }
                getProductLiveData.postValue(Result.success(array))
            } else {
                getProductLiveData.postValue(Result.error(error.message, ""))
            }

        }
    }
    fun getAllProductWhere(category:String) {
        getProductWhereLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_PRODUCT).whereEqualTo("category",category).addSnapshotListener { value, error ->
            if (error == null) {
                val array = arrayListOf<Product>()
                value!!.forEach {
                    array.add(it.toObject(Product::class.java))
                }
                getProductWhereLiveData.postValue(Result.success(array))
            } else {
                getProductWhereLiveData.postValue(Result.error(error.message, ""))
            }

        }
    }

    fun delete(productId: String) {
        deleteProductLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_PRODUCT).document(productId).delete().addOnFailureListener {
            deleteProductLiveData.postValue(Result.error(it.message, ""))
        }.addOnSuccessListener {
            deleteProductLiveData.postValue(Result.success(""))
        }
    }

    fun getProfile() {
        profileLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_PROFILE).document(COLLECTION_PROFILE).get().addOnSuccessListener {
            val profile = it.toObject(Profile::class.java)
            profileLiveData.postValue(Result.success(profile!!, ""))

        }.addOnFailureListener {
            profileLiveData.postValue(Result.error(it.message, ""))
        }
    }

    fun getAllCategory() {
        getCategoryLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_CATEGORY).addSnapshotListener { value, error ->
            if (error == null) {
                val array = arrayListOf<Category>()
                value!!.forEach {
                    array.add(it.toObject(Category::class.java))
                }
                getCategoryLiveData.postValue(Result.success(array))
            } else {
                getCategoryLiveData.postValue(Result.error(error.message, ""))
            }

        }
    }

    val getProductGetLiveData get() = getProductLiveData
    val getProductWhereGetLiveData get() = getProductWhereLiveData
    val deleteProductGetLiveData get() = deleteProductLiveData
    val profileGetLiveData get() = profileLiveData
    val getCategoryGetLiveData get() = getCategoryLiveData
    val getVersionLiveData get() = versionLiveData

}