package com.ps.omarmattr.abohesham.client.model

data class Ticket(
    val id:String = "",
    val user: User = User(),
    val userId: String = "",
    val quantity: Int = 0,
    val productId: String = "",
    val productName: String = "",
    val date:Long = 0L
)