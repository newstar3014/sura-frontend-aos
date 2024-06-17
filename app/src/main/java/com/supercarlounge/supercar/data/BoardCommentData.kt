package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

data class BoardCommentData(
    val cm_date: String,
    val cm_seq: Int,
    val cm_values: String,
    var like_yn: String,
    var m_like: Int,
    val m_notlike: Int,
    val u_gender: String,
    val u_nickname: String,
    val u_seq: Int,
    var u_staff_yn:String?,
    val sub_comment: ArrayList<SubComment>,





    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        parcel.readArrayList(SubComment::class.java.classLoader) as ArrayList<SubComment>,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cm_date)
        parcel.writeInt(cm_seq)
        parcel.writeString(cm_values)
        parcel.writeString(like_yn)
        parcel.writeInt(m_like)
        parcel.writeInt(m_notlike)
        parcel.writeString(u_gender)
        parcel.writeString(u_nickname)
        parcel.writeInt(u_seq)
        parcel.writeString(u_staff_yn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardCommentData> {
        override fun createFromParcel(parcel: Parcel): BoardCommentData {
            return BoardCommentData(parcel)
        }

        override fun newArray(size: Int): Array<BoardCommentData?> {
            return arrayOfNulls(size)
        }
    }

    fun getmessageText():String{
        if(!cm_values.isNullOrEmpty()) {
            return decodeset(cm_values!!)!!
        }else{
            return  ""
        }

    }
    fun decodeset(text: String?): String? {
        val myString: String? = null
        Log.d("체크테스트", text.toString())
        var t = text

        var index = 0

                if (t!!.contains("%")){

                    index = t?.indexOf("%")!!
                    var s =t.replace("%","")

                    return try {

                        var d = URLDecoder.decode(s, "UTF-8")

                        var t_stringBuilder = StringBuilder(d)
                        t_stringBuilder.insert(index,"%")
                        t_stringBuilder.toString()

                    } catch (e: UnsupportedEncodingException){
                        null
                    }
                }else{
                    return try {

                        URLDecoder.decode(t, "UTF-8")

                    } catch (e: UnsupportedEncodingException){
                        null
                    }
                }



    }
}

