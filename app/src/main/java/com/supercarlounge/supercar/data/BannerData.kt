package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class BannerData(var bl_name:String,
                      var bc_seq:Int,
                      var bl_seq:String,
                      var bc_title:String,
                      var bc_pic:String,
                      var bc_use:String,
                      var bc_link:String,
                      var bc_sday:String?,
                      var bc_eday:String?,
                      var bcdate:String?,
                      var bc_order:String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readInt()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bl_name)
        parcel.writeInt(bc_seq)
        parcel.writeString(bl_seq)
        parcel.writeString(bc_title)
        parcel.writeString(bc_pic)
        parcel.writeString(bc_use)
        parcel.writeString(bc_link)
        parcel.writeString(bc_sday)
        parcel.writeString(bc_eday)
        parcel.writeString(bcdate)
        parcel.writeString(bc_order)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BannerData> {
        override fun createFromParcel(parcel: Parcel): BannerData {
            return BannerData(parcel)
        }

        override fun newArray(size: Int): Array<BannerData?> {
            return arrayOfNulls(size)
        }
    }

}