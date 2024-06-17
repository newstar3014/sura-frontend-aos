package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class RecentDriveData(

    var b_seq  : Int?,
    var b_title: String?,
    var b_views: Int?,
    var like_count: Int?,
    var pc_confirm: Int?,
    var b_date: String?,
    var b_payment: String?,
    var b_same_gender: String?,
    var logCheck:String?,
    var likeCheck:String?,
    var profileCheck:String?
//    var viewImage: ArrayList<ImageData>?,
//    var likeImage: ArrayList<ImageData>?,
//    var confirmImage: ArrayList<ImageData>?,
    ):  Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
//        parcel.readArrayList(ImageData::class.java.classLoader) as ArrayList<ImageData>,
//        parcel.readArrayList(ImageData::class.java.classLoader) as ArrayList<ImageData>,
//        parcel.readArrayList(ImageData::class.java.classLoader) as ArrayList<ImageData>,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        b_seq?.let { parcel.writeInt(it) }
        parcel.writeString(b_title)
        b_views?.let { parcel.writeInt(it) }
        like_count?.let { parcel.writeInt(it) }
        pc_confirm?.let { parcel.writeInt(it) }
        parcel.writeString(b_date)
        parcel.writeString(b_payment)
        parcel.writeString(b_same_gender)
        parcel.writeString(logCheck)
        parcel.writeString(likeCheck)
        parcel.writeString(profileCheck)
//        parcel.writeList(viewImage)
//        parcel.writeList(likeImage)
//        parcel.writeList(confirmImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecentDriveData> {
        override fun createFromParcel(parcel: Parcel): RecentDriveData {
            return RecentDriveData(parcel)
        }

        override fun newArray(size: Int): Array<RecentDriveData?> {
            return arrayOfNulls(size)
        }
    }

}
