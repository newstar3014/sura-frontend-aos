package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import org.json.JSONArray
import org.json.JSONException

data class UserInfomationCompanyData(
    var u_seq: Int,
    val main_past_img: String?,
    val main_return: String?,
    val main_url: String?,
    val subImage: ArrayList<SubImage>,
    val u_about_me: String?,
    val u_area: String?,
    val u_email: String?,
    val u_image: String?,
    val u_name: String?,
    val u_nickname: String?,
    val u_type: String?,
    var u_about_me_ck: String?,
    var u_version:String?,
    var u_staff_yn:String?,
    var u_com_app:String?,
    var u_com_address:String?,
    var u_com_rep:String?,
    var u_com_web:String?,
    var u_com_parking:String?,
    var u_com_phone:String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(SubImage::class.java.classLoader) as ArrayList<SubImage>,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.writeInt(u_seq)
        parcel.writeString(main_past_img)
        parcel.writeString(main_return)
        parcel.writeString(main_url)
        parcel.writeList(subImage)
        parcel.writeString(u_about_me)
        parcel.writeString(u_area)
        parcel.writeString(u_email)
        parcel.writeString(u_image)
        parcel.writeString(u_name)
        parcel.writeString(u_nickname)
        parcel.writeString(u_type)
        parcel.writeString(u_about_me_ck)
        parcel.writeString(u_version)
        parcel.writeString(u_staff_yn)
        parcel.writeString(u_com_app)
        parcel.writeString(u_com_address)
        parcel.writeString(u_com_rep)
        parcel.writeString(u_com_web)
        parcel.writeString(u_com_parking)
        parcel.writeString(u_com_phone)
    }
    fun GetCompanyPhoneList():ArrayList<String>{
        var result :ArrayList<String>  = arrayListOf()
        if (u_com_phone?.isNullOrEmpty() == false) {
            var context = u_com_phone
            try {
                var d = JSONArray(u_com_phone)

                for (i in 0 until d.length()) {
                    var st = d[i].toString()
                    result.add(st)
                }

            }catch (e: JSONException){
                result.add(u_com_phone!!)
            }
        }


        return result
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInfomationCompanyData> {
        override fun createFromParcel(parcel: Parcel): UserInfomationCompanyData {
            return UserInfomationCompanyData(parcel)
        }

        override fun newArray(size: Int): Array<UserInfomationCompanyData?> {
            return arrayOfNulls(size)
        }
    }
}
