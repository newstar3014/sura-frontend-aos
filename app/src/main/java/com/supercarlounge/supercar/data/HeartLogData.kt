package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class HeartLogData(

    var hl_seq  : Int,
    var use_seq: Int,
    var hl_type: String,
    var u_seq: Int,
    var hl_count: Int,
    var hl_add_count: Int,
    var hl_date: String,
    var hl_payment: String,
    var hl_gender: String,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(hl_seq)
        parcel.writeInt(use_seq)
        parcel.writeString(hl_type)
        parcel.writeInt(u_seq)
        parcel.writeInt(hl_count)
        parcel.writeInt(hl_add_count)
        parcel.writeString(hl_date)
        parcel.writeString(hl_payment)
        parcel.writeString(hl_gender)
    }
    fun GetTypeText():String{
        var type = ""
        var count = ""
        if(hl_type.contains("PAY")){
            type = "하트"
            count = hl_add_count.toString()
            return type+count+" 개 구매"
        }else if (hl_type.contains("AUC")){
            type ="VIP 드라이브 사용"
            count = hl_count.toString()
        }else if(hl_type.contains("BUYPASS")){
            type ="드라이브 패스권 구입"
            count = hl_count.toString()
        }else if (hl_type.contains("프로필확인")){
            type = "프로필 열람"
            count = hl_count.toString()
        }else if (hl_type.contains("FEED")){
            type = "추천 프로필 열람"
            count = hl_count.toString()
        }else if (hl_type.contains("호감보내기")){
            type = "호감 보내기"
            count = hl_count.toString()
        }else if (hl_type.contains("관리자 지급")){
            type = "관리자 지급"
            count = hl_add_count.toString()
        }else if (hl_type.contains("이벤트")){
            type = "이벤트 증정"
            count = hl_add_count.toString()
        }else{
            type = hl_type
            count = hl_count.toString()
        }

        return count+"개 "+type
    }
    fun GetDateText():String{
        var result =""
        var x =hl_date.replace("T"," ")
        result = x.substring(0,x.length-3)

        return result
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HeartLogData> {
        override fun createFromParcel(parcel: Parcel): HeartLogData {
            return HeartLogData(parcel)
        }

        override fun newArray(size: Int): Array<HeartLogData?> {
            return arrayOfNulls(size)
        }
    }

}
