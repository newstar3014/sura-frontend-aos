package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class CompanionData(
    var ur_seq : Int,
    var s_seq: Int,
    var u_seq : Int,
    var ur_type: String,
    var use_seq : Int,
    var ur_text : String,
    var ur_date: String,

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ur_seq)
        parcel.writeInt(s_seq)
        parcel.writeInt(u_seq)
        parcel.writeString(ur_type)
        parcel.writeInt(use_seq)
        parcel.writeString(ur_text)
        parcel.writeString(ur_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompanionData> {
        override fun createFromParcel(parcel: Parcel): CompanionData {
            return CompanionData(parcel)
        }

        override fun newArray(size: Int): Array<CompanionData?> {
            return arrayOfNulls(size)
        }
    }
}




