package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

data class ProfileMatchingData(
    var type : String,
    var message : String,
    var pc_seq: String,
    var pc_status : String,
    var pc_ok : String,
    var safeNumber : String?,
    var p_phone_date : String?,
    var p_phone_end : String?,
    var u_rpm_open : String,
    var sub_rpm_open : String,
    var b_seq : String,
    var give_seq : String,
    var heart_seq : String,
    var re_nickname : String,
    var given_nickname : String,
    var pc_message : String?,
    var pc_b_message : String?,
    var now : String?,
    var subUser:String?,
    var isFriend:String?,
    var isflex:String?
    ):  Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(message)
        parcel.writeString(pc_seq)
        parcel.writeString(pc_status)
        parcel.writeString(pc_ok)
        parcel.writeString(safeNumber)
        parcel.writeString(p_phone_date)
        parcel.writeString(p_phone_end)
        parcel.writeString(u_rpm_open)
        parcel.writeString(sub_rpm_open)
        parcel.writeString(b_seq)
        parcel.writeString(give_seq)
        parcel.writeString(heart_seq)
        parcel.writeString(re_nickname)
        parcel.writeString(given_nickname)
        parcel.writeString(pc_message)
        parcel.writeString(pc_b_message)
        parcel.writeString(now)
        parcel.writeString(subUser)
        parcel.writeString(isFriend)
        parcel.writeString(isflex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileMatchingData> {
        override fun createFromParcel(parcel: Parcel): ProfileMatchingData {
            return ProfileMatchingData(parcel)
        }

        override fun newArray(size: Int): Array<ProfileMatchingData?> {
            return arrayOfNulls(size)
        }
    }
    fun getpc_b_message():String{
                if(!pc_b_message.isNullOrEmpty()) {
                    return decodeset(pc_b_message!!)!!
                }else{
                    return  ""
                }
    }
    fun getpc_message():String{
        if(!pc_message.isNullOrEmpty()) {
            return decodeset(pc_message!!)!!
        }else{
            return  ""
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
}
