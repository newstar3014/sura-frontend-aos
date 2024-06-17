package com.supercarlounge.supercar.data

import com.google.gson.annotations.SerializedName

data class InsertCommentData(
    @SerializedName("u_seq")
    var u_seq : Int,
    @SerializedName("b_seq")
    var b_seq: String,
    @SerializedName("cm_values")
    var cm_values: String,
    ){

}
