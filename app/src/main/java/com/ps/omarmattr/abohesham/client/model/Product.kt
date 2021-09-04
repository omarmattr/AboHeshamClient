package com.ps.omarmattr.abohesham.client.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val quantity: Int = 0,
    val oldPrice: Double = 0.0,
    val price: Double = 0.0,
    val category:String = "",
    val image: List<String> = arrayListOf(),
    val like:ArrayList<String> = arrayListOf()
):Parcelable
