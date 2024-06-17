package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class MySubInfoData (
    var msi_seq:Int,
    var mmi_seq:Int,
    var u_seq:Int,
    var msi_img:String?,
    var msi_contents:String?,
    var msi_date:String?,
    var select:Boolean
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(msi_seq)
        parcel.writeInt(mmi_seq)
        parcel.writeInt(u_seq)
        parcel.writeString(msi_img)
        parcel.writeString(msi_contents)
        parcel.writeString(msi_date)
        parcel.writeByte(if (select) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MySubInfoData> {
        override fun createFromParcel(parcel: Parcel): MySubInfoData {
            return MySubInfoData(parcel)
        }

        override fun newArray(size: Int): Array<MySubInfoData?> {
            return arrayOfNulls(size)
        }
    }
}