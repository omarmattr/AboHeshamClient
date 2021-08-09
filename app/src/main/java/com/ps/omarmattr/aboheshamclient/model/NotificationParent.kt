package com.ps.omarmattr.aboheshamclient.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.w3c.dom.Comment

data class NotificationParent(
    val data: Ticket = Ticket(),
    val to: String = ""
)

