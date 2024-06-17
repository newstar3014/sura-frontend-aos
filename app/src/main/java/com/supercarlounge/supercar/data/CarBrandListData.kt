package com.supercarlounge.supercar.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarBrandListData(
    @SerializedName("carb_image")
    val carb_image: String,
    @SerializedName("carb_name")
    val carb_name: String,
    @SerializedName("carb_seq")
    val carb_seq: Int
): Serializable