package com.supercarlounge.supercar.data

data class ResultBoardCommentData(
    var type:String,
    var message:String ,
    var totalcount:Int,
    var rows:ArrayList<BoardCommentData>) {
}