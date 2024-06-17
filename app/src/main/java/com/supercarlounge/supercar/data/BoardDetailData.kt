package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class BoardDetailData(
    val b_contents: String?,
    val b_cover: String?,
    val b_date: String?,
    var b_like: String?,
    val b_notlike: String?,
    val b_payment: String?,
    val b_profile: String?,
    val b_same_gender: String?,
    val b_seq: String?,
    val b_title: String?,
    val bookmark: String?,
    val c_seq: String?,
    val like_yn: String?,
    val rn_nickname: String?,
    val u_gender: String?,
    val u_nickname: String?,
    val u_seq: String?,
    val u_type: String?,
    var pa_data: String?,
    var u_staff_yn:String?
) :  Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(b_contents)
        parcel.writeString(b_cover)
        parcel.writeString(b_date)
        parcel.writeString(b_like)
        parcel.writeString(b_notlike)
        parcel.writeString(b_payment)
        parcel.writeString(b_profile)
        parcel.writeString(b_same_gender)
        parcel.writeString(b_seq)
        parcel.writeString(b_title)
        parcel.writeString(bookmark)
        parcel.writeString(c_seq)
        parcel.writeString(like_yn)
        parcel.writeString(rn_nickname)
        parcel.writeString(u_gender)
        parcel.writeString(u_nickname)
        parcel.writeString(u_seq)
        parcel.writeString(u_type)
        parcel.writeString(pa_data)
        parcel.writeString(u_staff_yn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardDetailData> {
        override fun createFromParcel(parcel: Parcel): BoardDetailData {
            return BoardDetailData(parcel)
        }

        override fun newArray(size: Int): Array<BoardDetailData?> {
            return arrayOfNulls(size)
        }
    }
}