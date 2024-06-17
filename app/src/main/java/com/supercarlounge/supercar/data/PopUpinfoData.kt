package com.supercarlounge.supercar.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable;

data class PopUpinfoData(
    @SerializedName("p_seq")
    var p_seq : Int,
    @SerializedName("p_pic")
    var p_pic: String,
//    @SerializedName("p_title")
//    var p_title : Int,
    @SerializedName("p_link")
    var p_link: String,
//    @SerializedName("p_startday")
//    var p_startday : Int,
//    @SerializedName("p_endday")
//    var p_endday : Int,
//    @SerializedName("p_state")
//    var p_state: String,
//    @SerializedName("p_order")
//    var p_order: String,
//    @SerializedName("p_date")
//    var p_date: String
    ):Serializable  {

}
