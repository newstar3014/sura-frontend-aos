package com.supercarlounge.supercar.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarModelListData(
    @SerializedName("carb_seq")
    val carb_seq: String,
    @SerializedName("carm_name")
    val carm_name: String,
    @SerializedName("carm_seq")
    val carm_seq: Int
): Serializable