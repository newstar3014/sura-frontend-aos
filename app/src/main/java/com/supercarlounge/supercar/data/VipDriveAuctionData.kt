package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class VipDriveAuctionData(

    var da_date : String,
    var da_seq: Int,
    var heart_count: Int,
    ):  Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(da_date)
        parcel.writeInt(da_seq)
        parcel.writeInt(heart_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VipDriveAuctionData> {
        override fun createFromParcel(parcel: Parcel): VipDriveAuctionData {
            return VipDriveAuctionData(parcel)
        }

        override fun newArray(size: Int): Array<VipDriveAuctionData?> {
            return arrayOfNulls(size)
        }
    }

}
