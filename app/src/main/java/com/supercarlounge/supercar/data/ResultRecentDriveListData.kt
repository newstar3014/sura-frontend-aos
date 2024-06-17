package com.supercarlounge.supercar.data

data class ResultRecentDriveListData(var type:String,
                                     var message:String,
                                     var totalPage:Int,
                                     var totalCount:Int,
                                     var rows:ArrayList<RecentDriveListData>) {


}