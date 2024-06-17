package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

data class SubComment(
    val cm_seq: Int,
    val cs_date: String,
    val cs_seq: Int,
    val cs_values: String,
    val css_seq: Int,
    var like_yn: String,
    val p_nickname: String,
    var s_like: Int,
    val s_notlike: Int,
    val u_gender: String,
    val u_nickname: String,
    var mainCommentPosition:Int,
    var u_staff_yn:String?,
    val u_seq: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cm_seq)
        parcel.writeString(cs_date)
        parcel.writeInt(cs_seq)
        parcel.writeString(cs_values)
        parcel.writeInt(css_seq)
        parcel.writeString(like_yn)
        parcel.writeString(p_nickname)
        parcel.writeInt(s_like)
        parcel.writeInt(s_notlike)
        parcel.writeString(u_gender)
        parcel.writeString(u_nickname)
        parcel.writeInt(mainCommentPosition)
        parcel.writeString(u_staff_yn)
        parcel.writeInt(u_seq)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubComment> {
        override fun createFromParcel(parcel: Parcel): SubComment {
            return SubComment(parcel)
        }

        override fun newArray(size: Int): Array<SubComment?> {
            return arrayOfNulls(size)
        }
    }

    fun getmessageText():String{
        if(!cs_values.isNullOrEmpty()) {
            return decodeset(cs_values!!)!!
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