package com.ps.omarmattr.aboheshamclient.model

data  class Ticket (val user:User = User(),val userId:String="",val quantity:Int=0,val productId:String = "",val productName:String= "")