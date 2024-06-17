package com.supercarlounge.supercar.data

data class ResultMatchingMiniData(
    var type:String,
    var message:String,
    var rows:ArrayList<MatchingMiniData>) {
}