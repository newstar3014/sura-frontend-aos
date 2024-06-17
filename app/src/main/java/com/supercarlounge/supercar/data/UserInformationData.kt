package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import kotlin.math.roundToInt

 data class UserInformationData(
     var u_seq: Int,
     val imType: String?,
     val im_seq: Int,
     val main_past_img: String?,
     val main_return: String?,
     val main_url: String?,
     val subImage: ArrayList<SubImage>,
     val u_about_me: String?,
     val u_age: String?,
     val u_area: String?,
     val u_birthday: String?,
     val u_charm: String?,
     val u_cm: String?,
     val u_drive: String?,
     val u_drive_url: String?,
     val u_email: String?,
     val u_gender: String?,
     val u_heart: Int,
     val u_image: String?,
     val u_job: String?,
     val u_lat: String?,
     val u_lock_screen: String?,
     val u_lon: String?,
     val u_music: String?,
     val u_name: String?,
     val u_nickname: String?,
     val u_personality: String?,
     val u_phone: String?,
     val u_smoking: String?,
     val u_tempAbout: String?,
     val u_tempData: String?,
     val u_tempReturn: String?,
     val u_type: String?,
     var distance:Int,
     var u_drive_w:String?,
     var u_drive_c:String?,
     var u_feed: String?,
     var u_about_me_ck: String?,
     var u_drive_w_yn: String?,
     var u_drive_c_yn: String?,
     var im_return: String?,
     var u_income:String?,
     var u_income_yn:String?,
     var u_loca: String?,
     var u_mt:String?,
     var u_mkt:String?,
     var u_code:String?,
     var checkupdate:String?,
     var u_version:String?,
     var u_tempAboutReturn:String?,
     var u_staff_yn:String?
 ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
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
        parcel.readInt(),
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
        parcel.readInt(),
                parcel.readString()!!,
        parcel.readString()!!,
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
        parcel.writeString(imType)
        parcel.writeInt(im_seq)
        parcel.writeString(main_past_img)
        parcel.writeString(main_return)
        parcel.writeString(main_url)
        parcel.writeList(subImage)
        parcel.writeString(u_about_me)
        parcel.writeString(u_age)
        parcel.writeString(u_area)
        parcel.writeString(u_birthday)
        parcel.writeString(u_charm)
        parcel.writeString(u_cm)
        parcel.writeString(u_drive)
        parcel.writeString(u_drive_url)
        parcel.writeString(u_email)
        parcel.writeString(u_gender)
        parcel.writeInt(u_heart)
        parcel.writeString(u_image)
        parcel.writeString(u_job)
        parcel.writeString(u_lat)
        parcel.writeString(u_lock_screen)
        parcel.writeString(u_lon)
        parcel.writeString(u_music)
        parcel.writeString(u_name)
        parcel.writeString(u_nickname)
        parcel.writeString(u_personality)
        parcel.writeString(u_phone)
        parcel.writeString(u_smoking)
        parcel.writeString(u_tempAbout)
        parcel.writeString(u_tempData)
        parcel.writeString(u_tempReturn)
        parcel.writeString(u_type)
        parcel.writeInt(distance)
        parcel.writeString(u_drive_w)
        parcel.writeString(u_drive_c)
        parcel.writeString(u_feed)
        parcel.writeString(u_about_me_ck)
        parcel.writeString(u_drive_w_yn)
        parcel.writeString(u_drive_c_yn)
        parcel.writeString(im_return)
        parcel.writeString(u_income)
        parcel.writeString(u_income_yn)
        parcel.writeString(u_loca)
        parcel.writeString(u_mt)
        parcel.writeString(u_mkt)
        parcel.writeString(u_code)
        parcel.writeString(checkupdate)
        parcel.writeString(u_version)
        parcel.writeString(u_tempAboutReturn)
        parcel.writeString(u_staff_yn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInformationData> {
        override fun createFromParcel(parcel: Parcel): UserInformationData {
            return UserInformationData(parcel)
        }

        override fun newArray(size: Int): Array<UserInformationData?> {
            return arrayOfNulls(size)
        }
    }
    fun GetDistanceSet():String{
        var result = ""
        if (distance> 1000){
            result = (distance /1000.0).roundToInt().toString()+"Km"
        }else{
            if (distance == 0){
                result = "??m"
            }else{
                result = distance.toString() +"m"
            }

        }
        return  result
    }
    fun GetSmokingText():String{
        var result = ""
        if (u_smoking.equals("Y")){
            result = "흡연"
        }else{
            result = "비흡연"
        }
        return  result
    }
    fun GetCharmlist():ArrayList<String>{
        var list = arrayListOf<String>()
        if(!u_charm.isNullOrEmpty()) {
            var data = u_charm.replace("\"", "").replace("[", "").replace("]", "")
            var datalist = data.split(",")
            list.addAll(datalist)

            Log.d("데이터타입4",list.toString())
        }
        return list
    }
    fun GetPersonerlist():ArrayList<String>{
        var list = arrayListOf<String>()
        if(!u_personality.isNullOrEmpty()) {
            var data = u_personality.replace("\"", "").replace("[", "").replace("]", "")
            var datalist = data.split(",")
            list.addAll(datalist)
        }
        return list
    }
    fun GetMusiclist():ArrayList<String>{
        var list = arrayListOf<String>()
        if(!u_music.isNullOrEmpty()) {
            var data = u_music.replace("\"", "").replace("[", "").replace("]", "")
            var datalist = data.split(",")

                list.addAll(datalist)
            Log.d("데이터타입 뮤직", list.toString())
        }
        return list
    }


}