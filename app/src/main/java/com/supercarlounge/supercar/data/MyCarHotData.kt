package com.supercarlounge.supercar.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyCarHotData(
    val mce_seq: String,
    val mce_type: String,
    val mce_criteria: String,
    val mce_time: String,
    val mce_benefit: String,
    val mce_guidelines: String,
    val mce_inquiry: String,
    val mce_date: String,
    val mce_popup: String?,
):Serializable