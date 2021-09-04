package com.ps.omarmattr.abohesham.client.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.w3c.dom.Comment

data class NotificationParent(
    val data :Data,
    val to: String = ""
)

data class Data(val data: Ticket= Ticket())

