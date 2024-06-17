package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class NotiData (
    var n_seq: String?,
    var u_seq: String?,
    var n_type: String?,
    var n_title: String?,
    var n_text: String?,
    var n_sday: String?,
    var n_eday: String?,
    var n_date: String?,
    var n_click: String?,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(n_seq)
        parcel.writeString(u_seq)
        parcel.writeString(n_type)
        parcel.writeString(n_title)
        parcel.writeString(n_text)
        parcel.writeString(n_sday)
        parcel.writeString(n_eday)
        parcel.writeString(n_date)
        parcel.writeString(n_click)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotiData> {
        override fun createFromParcel(parcel: Parcel): NotiData {
            return NotiData(parcel)
        }

        override fun newArray(size: Int): Array<NotiData?> {
            return arrayOfNulls(size)
        }
    }

    fun getTitle(): String{
        var s = ""

        if (n_type.equals("0")){
            s = "[공지]"
        }else  if (n_type.equals("1")){
            s = "[이벤트]"
        }else {
            s = "[제휴]"
        }

        return s
    }


}