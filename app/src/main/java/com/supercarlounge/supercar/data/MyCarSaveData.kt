package com.supercarlounge.supercar.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.supercarlounge.supercar.enumset.TimeValue
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.ParseException
import java.text.SimpleDateFormat

data class MyCarSaveData (var mainImageUrl:String,
                          var textmainTitle:String?,
                          var textmainContext:String,
                          var textIsMmiFirst:String,
                          var textIsMmiSecond:String,
                          var textIsMmithird:String,
                          var car_select_data:CarSelectData?,
                          var select_car_data_list :ArrayList<CarSelectData>,
                          var my_sub_info:ArrayList<MySubInfoData>)
