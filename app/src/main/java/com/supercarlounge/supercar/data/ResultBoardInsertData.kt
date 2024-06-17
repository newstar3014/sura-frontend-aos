package com.supercarlounge.supercar.data

data class ResultBoardInsertData(
    var type:String,
    var message:String,
    var rows:ArrayList<BoardInsertData> ) {
}