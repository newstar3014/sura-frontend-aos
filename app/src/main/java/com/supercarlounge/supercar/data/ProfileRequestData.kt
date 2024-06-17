package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class ProfileRequestData(

    var given_nickname : String,
    var pc_startdate: String,
    var isFriend: String,
    var pc_b_message: String,
    var pc_reqdate : String?,
    var pc_status : String?,
    var pc_seq : String?,
    var pc_ok : String?,
    var re_nickname : String?,
    var target_seq : Int?,
    var heart_seq : Int?,
    var sub_seq : Int?,
    var post_nickname : String?,
    var b_seq : Int?,
    var b_type : String?,
    var b_title : String?,
    var b_contents : String?,
    var u_type : String?,
    var u_image : String?,
    var u_gender1 : String?,
    var u_gender2 : String?,
    var realName : String?,
    var p_phone_end:String?,
    var im_past_img:String?
    ):  Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
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
        parcel.writeString(given_nickname)
        parcel.writeString(pc_startdate)
        parcel.writeString(isFriend)
        parcel.writeString(pc_b_message)
        parcel.writeString(pc_reqdate)
        parcel.writeString(pc_status)
        parcel.writeString(pc_seq)
        parcel.writeString(pc_ok)
        parcel.writeString(re_nickname)
        parcel.writeValue(target_seq)
        parcel.writeValue(heart_seq)
        parcel.writeValue(sub_seq)
        parcel.writeString(post_nickname)
        parcel.writeValue(b_seq)
        parcel.writeString(b_type)
        parcel.writeString(b_title)
        parcel.writeString(b_contents)
        parcel.writeString(u_type)
        parcel.writeString(u_image)
        parcel.writeString(u_gender1)
        parcel.writeString(u_gender2)
        parcel.writeString(realName)
        parcel.writeString(p_phone_end)
        parcel.writeString(im_past_img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileRequestData> {
        override fun createFromParcel(parcel: Parcel): ProfileRequestData {
            return ProfileRequestData(parcel)
        }

        override fun newArray(size: Int): Array<ProfileRequestData?> {
            return arrayOfNulls(size)
        }
    }

    fun getImage():String{
        var src =""
        if (pc_ok?.equals("Y") == true){
            src = u_image.toString()
        }
        return src
    }
}
