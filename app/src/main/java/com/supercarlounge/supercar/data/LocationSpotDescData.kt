package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class LocationSpotDescData(

    var title : String?,
    var content: String?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationSpotDescData> {
        override fun createFromParcel(parcel: Parcel): LocationSpotDescData {
            return LocationSpotDescData(parcel)
        }

        override fun newArray(size: Int): Array<LocationSpotDescData?> {
            return arrayOfNulls(size)
        }
    }
}
