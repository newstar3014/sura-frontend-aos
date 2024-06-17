package com.supercarlounge.supercar.data

data class DrivePostLIstData(

    var name : String,
    var sex: String,
    var titletext: String,
    var subtext: String,
    var time: String,
    var like: String,
    var distance: String,
    val subComments: ArrayList<CommentSubData>
    ){

}
