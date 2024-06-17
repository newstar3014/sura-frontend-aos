package com.supercarlounge.supercar.data

data class ResultMyCarHotData(
    var type:String,
    var message:String ,
    var rows:ArrayList<MyCarHotData>) {
}