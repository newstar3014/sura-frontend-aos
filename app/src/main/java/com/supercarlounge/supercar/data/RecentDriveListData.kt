package com.supercarlounge.supercar.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.supercarlounge.supercar.enumset.TimeValue
import java.text.ParseException
import java.text.SimpleDateFormat

data class RecentDriveListData(
    //2
    var bl_seq  : Int,
    var b_seq: Int,
    var u_seq: Int,
    var u_nickname: String,
    var im_url: String,
    var b_title: String,
    var b_payment: String,
    var date: String,
    var mypage_view: String,
    //1
    var pc_seq:Int,
    //3
    var blo_seq:Int,
    var im_past_img:String



    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bl_seq)
        parcel.writeInt(b_seq)
        parcel.writeInt(u_seq)
        parcel.writeString(u_nickname)
        parcel.writeString(im_url)
        parcel.writeString(b_title)
        parcel.writeString(b_payment)
        parcel.writeString(date)
        parcel.writeString(mypage_view)
        parcel.writeInt(pc_seq)
        parcel.writeInt(blo_seq)
        parcel.writeString(im_past_img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecentDriveListData> {
        override fun createFromParcel(parcel: Parcel): RecentDriveListData {
            return RecentDriveListData(parcel)
        }

        override fun newArray(size: Int): Array<RecentDriveListData?> {
            return arrayOfNulls(size)
        }
    }
    fun setTime() :String{
        var time = dateTimeToMillSec(date)
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
                if (diffTime < i.maximum) {
                    msg=i.msg
                    break
                }
            }
        }
        return diffTime.toString() + msg.toString()
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


}
