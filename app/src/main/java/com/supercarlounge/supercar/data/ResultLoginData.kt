package com.supercarlounge.supercar.data

data class ResultLoginData(
    val type: String?,
    val message: String?,
    val u_admin: String?,
    var u_seq: String?,

    var status: String?,
    var token: String?,
    var expiresIn: String?,
    var data:UserData)