package com.supercarlounge.supercar.data

data class ResultDrivePassData(

    var type  : String,
    var message: String,
    var expDate: String,
    var pass_day: String,

    ){
        fun getdate():String{
            var dateset = ""
            if(expDate.length>10) {
                dateset = expDate.substring(2, 10)
            }
            return dateset
        }
}
