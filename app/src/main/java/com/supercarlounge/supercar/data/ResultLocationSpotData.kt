package com.supercarlounge.supercar.data

data class ResultLocationSpotData(
    var type:String,
    var message:String,
    var totalCount:Int?,
    var totalPage:Int?,
    var rows:ArrayList<LocationSpotData>) {
}