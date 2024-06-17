package com.supercarlounge.supercar.data

data class ResultDriveData(var type:String,
                           var message:String,
                           var totalPage:Int,
                           var totalCount:Int,
                           var drives:ArrayList<DriveAwayData>) {


}