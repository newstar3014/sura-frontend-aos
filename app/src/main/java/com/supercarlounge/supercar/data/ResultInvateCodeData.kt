package com.supercarlounge.supercar.data

data class ResultInvateCodeData(
    var type:String,
    var message:String,
    var rows:ArrayList<InviteCodeData>) {
}