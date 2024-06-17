package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable

data class PointMallLogData(

    var pol_seq  : String,
    var point_check: String?,
    var point_type: String?,
    var point_type_seq: String?,
    var point_sub_type: String?,
    var point_sub_seq: String?,
    var poi_seq: String?,
    var u_seq: String?,
    var now_point: String?,
    var pol_point: String?,
    var change_point: String?,
    var point_use: String?,
    var point_now: String?,
    var pol_date: String?,
    var goodsName:String?,
    var brandName:String?
    ): Parcelable {
    constructor(parcel: Parcel) : this(
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

    fun GetLogtext():String{

        if (point_check.equals("1")){
         when(point_sub_type){
             "LIKE" ->{
                return  "글 추천"
             }
             "SAVE" ->{
                 return  "게시글 작성"
             }
             "COMMENT","SUBCOMMENT"->{
                 return  "댓글 작성"
             }
             "MATCHING"->{
                 return  "호감 보내기"
             }
             "ADMIN" ->{
                 return "관리자 지급"
             }

         }
        }else if(point_check.equals("2")){
            var goods = goodsName
            if (goods.isNullOrEmpty()) {
                goods = "상품 구입"
            }
            return goods
        }else {
            return  "회수"
        }
        return ""
    }
    fun GetDateText():String{
        var result =""
        var x =pol_date!!.replace("T"," ")
        result = x.substring(0,x.length-3)

        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pol_seq)
        parcel.writeString(point_check)
        parcel.writeString(point_type)
        parcel.writeString(point_type_seq)
        parcel.writeString(point_sub_type)
        parcel.writeString(point_sub_seq)
        parcel.writeString(poi_seq)
        parcel.writeString(u_seq)
        parcel.writeString(now_point)
        parcel.writeString(pol_point)
        parcel.writeString(change_point)
        parcel.writeString(point_use)
        parcel.writeString(point_now)
        parcel.writeString(pol_date)
        parcel.writeString(goodsName)
        parcel.writeString(brandName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PointMallLogData> {
        override fun createFromParcel(parcel: Parcel): PointMallLogData {
            return PointMallLogData(parcel)
        }

        override fun newArray(size: Int): Array<PointMallLogData?> {
            return arrayOfNulls(size)
        }
    }
}
