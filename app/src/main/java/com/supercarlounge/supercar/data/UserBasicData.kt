package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class UserBasicData(
    var u_name:String,
    var u_gender:String,
    var u_birthday:String,
    var u_phone:String,
    ) :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(u_name)
        parcel.writeString(u_gender)
        parcel.writeString(u_birthday)
        parcel.writeString(u_phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserBasicData> {
        override fun createFromParcel(parcel: Parcel): UserBasicData {
            return UserBasicData(parcel)
        }

        override fun newArray(size: Int): Array<UserBasicData?> {
            return arrayOfNulls(size)
        }
    }
}