package com.supercarlounge.supercar.data

data class ResultRecentDriveData(
    var type:String,
    var message:String,
    var totalCount:Int,
    var rows:ArrayList<RecentDriveData>) {
}