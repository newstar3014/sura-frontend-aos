package com.supercarlounge.supercar.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarSubModelListData(
    @SerializedName("carm_seq")
    val carm_seq: Int,
    @SerializedName("csm_name")
    val csm_name: String,
    @SerializedName("csm_seq")
    val csm_seq: Int
):Serializable