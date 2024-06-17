package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class DriveAuctionData(

    var da_seq  : Int,
    var b_seq: String,
    var u_seq: String,
    var heart_count: Int,
    var da_date: String,
    var end_date: String,
    var push_check: String,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(da_seq)
        parcel.writeString(b_seq)
        parcel.writeString(u_seq)
        parcel.writeInt(heart_count)
        parcel.writeString(da_date)
        parcel.writeString(end_date)
        parcel.writeString(push_check)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DriveAuctionData> {
        override fun createFromParcel(parcel: Parcel): DriveAuctionData {
            return DriveAuctionData(parcel)
        }

        override fun newArray(size: Int): Array<DriveAuctionData?> {
            return arrayOfNulls(size)
        }
    }

}
