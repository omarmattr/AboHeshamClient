package com.ps.omarmattr.abohesham.client.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ps.omarmattr.abohesham.client.model.NotificationParent
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.model.Ticket
import com.ps.omarmattr.abohesham.client.model.User
import com.ps.omarmattr.abohesham.client.other.COLLECTION_PRODUCT
import com.ps.omarmattr.abohesham.client.other.COLLECTION_TICKET
import com.ps.omarmattr.abohesham.client.other.COLLECTION_USER
import com.ps.omarmattr.abohesham.client.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LikeRepository @Inject constructor() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val likeLiveData: MutableLiveData<Result<Any>> = MutableLiveData()

    fun getUserLike(id: String) {
        likeLiveData.postValue(Result.loading(""))
        db.collection(COLLECTION_PRODUCT).whereArrayContains("like", id)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val array = arrayListOf<Product>()
                    value!!.forEach {
                        array.add(it.toObject(Product::class.java))
                    }
                    if(array.isEmpty()){
                        likeLiveData.postValue(Result.empty(""))
                    }else{
                        likeLiveData.postValue(Result.success(array))

                    }
                } else {
                    likeLiveData.postValue(Result.error(error.message, ""))
                }

            }
    }

    val getLikeLiveData get() = likeLiveData

}