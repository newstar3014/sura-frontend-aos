package com.supercarlounge.supercar.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.supercarlounge.supercar.enumset.TimeValue
import java.text.ParseException
import java.text.SimpleDateFormat

data class MyCarBoardSubCommentData(
    val msc_seq: Int,
    val u_seq: Int,
    val mmi_seq: Int,
    val mmc_seq: Int,
    val mrc_seq: Int,
    var mrc_nickname: String?,
    val msc_values: String?,
    var msc_date: String?,
    val msc_sub_seq: Int,
    val u_gender: String?,
    val p_nickname: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(msc_seq)
        parcel.writeInt(u_seq)
        parcel.writeInt(mmi_seq)
        parcel.writeInt(mmc_seq)
        parcel.writeInt(mrc_seq)
        parcel.writeString(mrc_nickname)
        parcel.writeString(msc_values)
        parcel.writeString(msc_date)
        parcel.writeInt(msc_sub_seq)
        parcel.writeString(u_gender)
        parcel.writeString(p_nickname)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyCarBoardSubCommentData> {
        override fun createFromParcel(parcel: Parcel): MyCarBoardSubCommentData {
            return MyCarBoardSubCommentData(parcel)
        }

        override fun newArray(size: Int): Array<MyCarBoardSubCommentData?> {
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
    fun getTagNickname():String{
        var tagName = "@$p_nickname"
        return  tagName
    }
}