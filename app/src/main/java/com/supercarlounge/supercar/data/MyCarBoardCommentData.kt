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

data class MyCarBoardCommentData(
    val mmc_seq: Int,
    val mmi_seq: Int,
    val u_seq: Int,
    var mrc_seq: Int,
    var mrc_nickname: String?,
    val mmc_values: String?,
    val mmc_date: String?,
    val mmc_like: Int,
    val u_gender: String?,
    val sub_comment: ArrayList<MyCarBoardSubCommentData>,





    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readArrayList(MyCarBoardSubCommentData::class.java.classLoader) as ArrayList<MyCarBoardSubCommentData>,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mmc_seq)
        parcel.writeInt(mmi_seq)
        parcel.writeInt(u_seq)
        parcel.writeInt(mrc_seq)
        parcel.writeString(mrc_nickname)
        parcel.writeString(mmc_values)
        parcel.writeString(mmc_date)
        parcel.writeInt(mmc_like)
        parcel.writeString(u_gender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyCarBoardCommentData> {
        override fun createFromParcel(parcel: Parcel): MyCarBoardCommentData {
            return MyCarBoardCommentData(parcel)
        }

        override fun newArray(size: Int): Array<MyCarBoardCommentData?> {
            return arrayOfNulls(size)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target," ") // '-'를 공백으로 바꿔준다.
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(result)

            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("변환된 시간", timeInMilliseconds.toString())
        return timeInMilliseconds
    }


    fun setTime(time : Long) :String{
        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        var diffTime = (curTime- time) / 1000
        var msg: String? = null
        if(diffTime < TimeValue.SEC.value ) {
            msg = "방금 전"
            return msg.toString()
        }else {
            for (i in TimeValue.values()) {
                diffTime = diffTime.div(i.value)


                if (diffTime > 14 && i == TimeValue.HOUR ||i > TimeValue.HOUR){
                    msg = "지난글"
                    return msg.toString()
                }
                if (diffTime < i.maximum) {
                    msg=i.msg
                    break
                }
            }
        }
        return diffTime.toString() + msg.toString()
    }
}

