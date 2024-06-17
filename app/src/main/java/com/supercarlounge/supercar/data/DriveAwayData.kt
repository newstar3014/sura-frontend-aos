package com.supercarlounge.supercar.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.supercarlounge.supercar.enumset.TimeValue
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.ParseException
import java.text.SimpleDateFormat

data class DriveAwayData(
    var b_seq: Int,
    var b_type: String?,
    var c_seq: String?,
    var ub_type: String?,
    var u_seq: Int,
    var ri_seq: Int,
    var rn_nickname: String?,
    var u_image: String?,
    var b_cover: String?,
    var b_cover_gray: String?,
    var b_title: String?,
    var b_contents: String?,
    var uc_seq: Int,
    var uc_brand: Int,
    var open_lounge: String?,
    var views: Int,
    var b_blind: String?,
    var b_profile: String?,
    var b_payment: String?,
    var b_same_gender: String?,
    var b_date: String?,
    var b_love: Int,
    var like_check: String?,
    var distance: String? = null,
    var u_lon: String? = null,
    var u_lat: String? = null,
    var likeCount: Int,
    var carb_name: String? = "PASSENGER",
    var carb_image: String? = null,
    var u_gender: String?,
    var like_count: String?,
    var my_like: String?,
    var heart_count: Int = 0,
    var pc_status: Int,
    var pc_ok: String?,
    var b_how: Int,
    var b_when: Int,
    var heart_seq: Int,
    var spot_seq: String?,
    var meeting_day: String?,
    var spot_lon: String?,
    var spot_lat: String?,
    var spot_address: String?,
    var spot_detail_address: String?,
    var spot_title: String?
) : Parcelable {
    var check_like: MutableLiveData<String> = MutableLiveData<String>(like_check)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(b_seq)
        parcel.writeString(b_type)
        parcel.writeString(c_seq)
        parcel.writeString(ub_type)
        parcel.writeInt(u_seq)
        parcel.writeInt(ri_seq)
        parcel.writeString(rn_nickname)
        parcel.writeString(u_image)
        parcel.writeString(b_cover)
        parcel.writeString(b_cover_gray)
        parcel.writeString(b_title)
        parcel.writeString(b_contents)
        parcel.writeInt(uc_seq)
        parcel.writeInt(uc_brand)
        parcel.writeString(open_lounge)
        parcel.writeInt(views)
        parcel.writeString(b_blind)
        parcel.writeString(b_profile)
        parcel.writeString(b_payment)
        parcel.writeString(b_same_gender)
        parcel.writeString(b_date)
        parcel.writeInt(b_love)
        parcel.writeString(like_check)
        parcel.writeString(distance)
        parcel.writeString(u_lon)
        parcel.writeString(u_lat)
        parcel.writeInt(likeCount)
        parcel.writeString(carb_name)
        parcel.writeString(carb_image)
        parcel.writeString(u_gender)
        parcel.writeString(like_count)
        parcel.writeString(my_like)
        parcel.writeInt(heart_count)
        parcel.writeInt(pc_status)
        parcel.writeString(pc_ok)
        parcel.writeInt(b_how)
        parcel.writeInt(b_when)
        parcel.writeInt(heart_seq)
        parcel.writeString(spot_seq)
        parcel.writeString(meeting_day)
        parcel.writeString(spot_lon)
        parcel.writeString(spot_lat)
        parcel.writeString(spot_address)
        parcel.writeString(spot_detail_address)
        parcel.writeString(spot_title)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        var d = b_seq.toInt()
        var x: Int = (other as DriveAwayData).b_seq.toInt()

        return b_seq == (other as DriveAwayData).b_seq
    }

    companion object CREATOR : Parcelable.Creator<DriveAwayData> {
        override fun createFromParcel(parcel: Parcel): DriveAwayData {
            return DriveAwayData(parcel)
        }

        override fun newArray(size: Int): Array<DriveAwayData?> {
            return arrayOfNulls(size)
        }
    }

    fun getSeclet(): String {
        return "PASSENGER"
    }

    fun setTime(): String {
        if (b_date != null) {
            var time = dateTimeToMillSec(b_date!!)
            val curTime = System.currentTimeMillis()
            Log.d("현재 시간", curTime.toString())
            var diffTime = (curTime - time) / 1000
            var msg: String? = null
            if (diffTime < TimeValue.SEC.value) {
                msg = "방금 전"
                return msg.toString()
            } else {
                for (i in TimeValue.values()) {
                    diffTime = diffTime.div(i.value)


                    if (diffTime > 14 && i == TimeValue.HOUR || i > TimeValue.HOUR) {
                        msg = "지난글"
                        return msg.toString()
                    }
                    if (diffTime < i.maximum) {
                        msg = i.msg
                        break
                    }
                }
            }
            return diffTime.toString() + msg.toString()
        }
        return ""


    }


    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long {
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target, " ") // '-'를 공백으로 바꿔준다.
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

    fun getlike_check(): Boolean {
        return like_check.equals("1")
    }

    fun GetTitle(): String {
        if (!b_title.isNullOrEmpty()) {
            return b_title.toString()
        } else {
            return ""
        }

    }

    fun GetContent(): String {

        if (!b_contents.isNullOrEmpty()) {
            return b_contents.toString()
        } else {
            return ""
        }

    }

    fun decodeset(text: String?): String? {
        val myString: String? = null
        return try {

            URLDecoder
                .decode(
                    text, "UTF-8"
                )
        } catch (e: UnsupportedEncodingException) {
            text
        }
    }


    fun getDistans(): String? {
        if (distance != null) {
            var rdistance = ""
            if (distance?.length!! > 4 || distance.toString().equals("-1")) {
                rdistance = "위치 정보없음"
            } else {
                rdistance = distance.toString()
            }
            return rdistance
        } else {
            return null
        }

    }

    fun getBoardType(): String? {
        var result = ""
        if (ub_type.equals("DRIVER")) {
            if (carb_name != null) {
                result = carb_name.toString()
            } else {
                result = "SECRET"
            }

        } else {
            result = "PASSENGER"
        }
        return result
    }
}


