package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class ViewListData(
    var target_seq: String,
    val rn_nickname: String,
    val u_image: String,
    val pc_ok: String,
    val pc_status: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(target_seq)
        parcel.writeString(rn_nickname)
        parcel.writeString(u_image)
        parcel.writeString(pc_ok)
        parcel.writeString(pc_status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ViewListData> {
        override fun createFromParcel(parcel: Parcel): ViewListData {
            return ViewListData(parcel)
        }

        override fun newArray(size: Int): Array<ViewListData?> {
            return arrayOfNulls(size)
        }
    }
}