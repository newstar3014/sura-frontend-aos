package com.supercarlounge.supercar.data

data class ResultTokenData(var status:String,
                           var token:String,
                           var expiresin:Int,
                           var data: UserData)
                           {


}