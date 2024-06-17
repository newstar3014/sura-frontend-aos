package com.supercarlounge.supercar.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.supercarlounge.supercar.enumset.TimeValue
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.ParseException
import java.text.SimpleDateFormat

data class SampleImageData (var di_seq :String,
                            var di_url :String,
var di_yn  :String)
                            : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(di_seq)
        parcel.writeString(di_url)
        parcel.writeString(di_yn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SampleImageData> {
        override fun createFromParcel(parcel: Parcel): SampleImageData {
            return SampleImageData(parcel)
        }

        override fun newArray(size: Int): Array<SampleImageData?> {
            return arrayOfNulls(size)
        }
    }


}