package com.supercarlounge.supercar.data

data class ResultVipDriveAuctionData(
    var type:String,
    var message:String,
    var rows:ArrayList<VipDriveAuctionData>) {
}