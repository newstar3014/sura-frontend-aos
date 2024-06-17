package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable


data class ProfileSuggestionData(
    var u_seq:Int,
    var u_image:String,
    var carb_image:String?,
    var carb_name:String?,
    var u_drive :String?,
    var u_charm  :String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
                parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(u_seq)
        parcel.writeString(u_image)
        parcel.writeString(carb_image)
        parcel.writeString(carb_name)
        parcel.writeString(u_drive)
        parcel.writeString(u_charm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileSuggestionData> {
        override fun createFromParcel(parcel: Parcel): ProfileSuggestionData {
            return ProfileSuggestionData(parcel)
        }

        override fun newArray(size: Int): Array<ProfileSuggestionData?> {
            return arrayOfNulls(size)
        }
    }


}




