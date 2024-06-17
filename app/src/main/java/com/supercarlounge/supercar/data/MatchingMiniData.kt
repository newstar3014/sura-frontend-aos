package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class MatchingMiniData(

    var target_seq : Int,
    var last_day: Int,
    var last_time: Int,
    var u_seq: Int,
    var sub_seq: Int,
    var b_seq: Int,
    var mypage_view: String,
    var receive_view: String,
    var re_nickname: String,
    var given_nickname: String,
    var heart_seq: Int,
    var im_url: String,
    var realName: String,
    var pc_ok: String,
    var im_past_img: String,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(target_seq)
        parcel.writeInt(last_day)
        parcel.writeInt(last_time)
        parcel.writeInt(u_seq)
        parcel.writeInt(sub_seq)
        parcel.writeInt(b_seq)
        parcel.writeString(mypage_view)
        parcel.writeString(receive_view)
        parcel.writeString(re_nickname)
        parcel.writeString(given_nickname)
        parcel.writeInt(heart_seq)
        parcel.writeString(im_url)
        parcel.writeString(realName)
        parcel.writeString(pc_ok)
        parcel.writeString(im_past_img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MatchingMiniData> {
        override fun createFromParcel(parcel: Parcel): MatchingMiniData {
            return MatchingMiniData(parcel)
        }

        override fun newArray(size: Int): Array<MatchingMiniData?> {
            return arrayOfNulls(size)
        }
    }

}
