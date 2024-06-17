package com.supercarlounge.supercar.data

data class ResultBoardData(var type:String,
                           var message:String,
                           var totalPage:Int,
                           var totalCount:Int,
                           var rows:ArrayList<BoardData>) {


}