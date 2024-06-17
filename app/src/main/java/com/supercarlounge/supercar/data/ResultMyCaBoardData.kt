package com.supercarlounge.supercar.data

data class ResultMyCaBoardData(var type:String,
                               var message:String,
                               var totalCount:Int,
                               var totalPage:Int,
                               var rows:ArrayList<MyCarBoardData>) {


}