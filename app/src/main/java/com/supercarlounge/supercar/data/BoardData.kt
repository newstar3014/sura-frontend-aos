package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

data class BoardData(
    val b_blind: String,
    val b_comment: Int,
    val b_contents: String,
    val b_cover: String,
    val b_cover_gray: String,
    val b_date: String?,
    val b_like: Int,
    val b_love: Int,
    val b_notlike: Int,
    val b_payment: String,
    val b_profile: String,
    val b_same_gender: String,
    val b_seq: Int,
    val b_title: String,
    val b_type: String,
    val bookmark: String,
    val c_seq: String,
    val like_check: String,
    val open_lounge: String,
    val ri_seq: Int,
    val rn_nickname: String,
    val u_gender: String,
    val u_image: String,
    val u_seq: Int,
    val ub_type: String,
    val uc_brand: Int,
    val uc_seq: Int,
    val views: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(b_blind)
        parcel.writeInt(b_comment)
        parcel.writeString(b_contents)
        parcel.writeString(b_cover)
        parcel.writeString(b_cover_gray)
        parcel.writeString(b_date)
        parcel.writeInt(b_like)
        parcel.writeInt(b_love)
        parcel.writeInt(b_notlike)
        parcel.writeString(b_payment)
        parcel.writeString(b_profile)
        parcel.writeString(b_same_gender)
        parcel.writeInt(b_seq)
        parcel.writeString(b_title)
        parcel.writeString(b_type)
        parcel.writeString(bookmark)
        parcel.writeString(c_seq)
        parcel.writeString(like_check)
        parcel.writeString(open_lounge)
        parcel.writeInt(ri_seq)
        parcel.writeString(rn_nickname)
        parcel.writeString(u_gender)
        parcel.writeString(u_image)
        parcel.writeInt(u_seq)
        parcel.writeString(ub_type)
        parcel.writeInt(uc_brand)
        parcel.writeInt(uc_seq)
        parcel.writeInt(views)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardData> {
        override fun createFromParcel(parcel: Parcel): BoardData {
            return BoardData(parcel)
        }

        override fun newArray(size: Int): Array<BoardData?> {
            return arrayOfNulls(size)
        }
    }

//    fun getmessageTitle():String{
//        if(!b_title.isNullOrEmpty()) {
//            return decodeset(b_title!!)!!
//        }else{
//            return  ""
//        }
//
//    }
//
//    fun getmessageText():String{
//        if(!b_contents.isNullOrEmpty()) {
//            return decodeset(b_contents!!)!!
//        }else{
//            return  ""
//        }
//
//    }
//    fun decodeset(text: String?): String? {
//        val myString: String? = null
//        return try {
//
//            URLDecoder
//                .decode(
//                    text, "UTF-8"
//                )
//        } catch (e: UnsupportedEncodingException) {
//            text
//        }
//    }
}