package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable


data class TokenData(

    var u_seq : Int,
    var u_admin: String,
    var u_uuid: String,
    var u_type : String,
    var u_birthday : String,
    var u_gender: String,
    var u_police: Int,
    var u_image : String,

    ):    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(u_seq)
        parcel.writeString(u_admin)
        parcel.writeString(u_uuid)
        parcel.writeString(u_type)
        parcel.writeString(u_birthday)
        parcel.writeString(u_gender)
        parcel.writeInt(u_police)
        parcel.writeString(u_image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TokenData> {
        override fun createFromParcel(parcel: Parcel): TokenData {
            return TokenData(parcel)
        }

        override fun newArray(size: Int): Array<TokenData?> {
            return arrayOfNulls(size)
        }
    }


}
