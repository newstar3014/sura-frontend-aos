package com.supercarlounge.supercar.data

data class CommentData(

    var name : String,
    var sex: String,
    var comment: String,
    var time: String,
    var like: String,
    val subComments: ArrayList<CommentSubData>
    ){

}
