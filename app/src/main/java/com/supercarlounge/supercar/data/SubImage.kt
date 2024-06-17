package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class SubImage(
    var im_date: String?,
    var im_past_img: String?,
    var im_return: String?,
    var im_seq: Int,
    var im_type: String?,
    var im_url: String?,
    var u_seq: Int,
    var im_before_Url: String?,
    var new_Url: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(im_date)
        parcel.writeString(im_past_img)
        parcel.writeString(im_return)
        parcel.writeInt(im_seq)
        parcel.writeString(im_type)
        parcel.writeString(im_url)
        parcel.writeInt(u_seq)
        parcel.writeString(im_before_Url)
        parcel.writeString(new_Url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubImage> {
        override fun createFromParcel(parcel: Parcel): SubImage {
            return SubImage(parcel)
        }

        override fun newArray(size: Int): Array<SubImage?> {
            return arrayOfNulls(size)
        }
    }
}