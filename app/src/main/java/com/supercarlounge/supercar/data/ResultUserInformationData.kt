package com.supercarlounge.supercar.data

data class ResultUserInformationData(
    val car: ArrayList<ArrayList<UserInformationCarData>>,
    val message: String,
    val rows: ArrayList<UserInformationData>,
    val type: String
)