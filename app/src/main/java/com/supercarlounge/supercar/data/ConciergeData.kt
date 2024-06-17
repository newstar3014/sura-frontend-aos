package com.supercarlounge.supercar.data

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import org.json.JSONArray
import org.json.JSONException

data class ConciergeData(

    var co_seq : Int,
    var cc_seq: Int,
    var co_image: String?,
    var co_image_brand: String?,
    var co_company : String,
    var isView: Int,
    var co_normal: String?,
    var co_vip: String?,
    var co_text: String?,
    var co_link : String?,
    var co_date: String?,
    var cc_type: String,
    var co_info: String?,
    var co_email: String?,
    var co_link_type : String?,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(co_seq)
        parcel.writeInt(cc_seq)
        parcel.writeString(co_image)
        parcel.writeString(co_image_brand)
        parcel.writeString(co_company)
        parcel.writeInt(isView)
        parcel.writeString(co_normal)
        parcel.writeString(co_vip)
        parcel.writeString(co_text)
        parcel.writeString(co_link)
        parcel.writeString(co_date)
        parcel.writeString(cc_type)
        parcel.writeString(co_info)
        parcel.writeString(co_email)
        parcel.writeString(co_link_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConciergeData> {
        override fun createFromParcel(parcel: Parcel): ConciergeData {
            return ConciergeData(parcel)
        }

        override fun newArray(size: Int): Array<ConciergeData?> {
            return arrayOfNulls(size)
        }
    }

    fun Getnormal():String{
        var result = ""
        var noraml = co_normal
        if(noraml.isNullOrEmpty()){
            noraml = "별도문의"
        }
        result = "⦁  일반회원 혜택 :$noraml"
        return result
    }
    fun GetVip():String{
        var result = ""
        var vip = co_vip
        if(vip.isNullOrEmpty()){
            vip = "별도문의"
        }
        result = "⦁  VIP회원 혜택 : $vip"
        return result
    }

    fun Getdate():String{
        var result = ""
        if (co_date?.isNotEmpty() == true){
            result = "⦁  "+co_date.toString()
        }else{
            result = "⦁  "+"추후 공지시 까지"
        }
        return result
    }
    fun Getsetpay():String{
        var result= ""
        result += "일반회원 : "+co_normal+"\n"
        result+= "VIP회원 : "+co_vip
        return result
    }
    fun coTextList():ArrayList<String>{
        var result :ArrayList<String>  = arrayListOf()
        var word ='"'
        if (co_text?.isNullOrEmpty() == false) {
            Log.d("테스트1", co_text.toString())
            var context = co_text
            try {
                var d = JSONArray(co_text)

                for (i in 0 until d.length()) {
                    var st = d[i].toString()
                    result.add(st)
                }

            }catch (e:JSONException){
                result.add(co_text!!)
            }
        }


        return result
    }
    fun coTextString():String{

        var word =""
        if (co_text?.isNullOrEmpty() == false) {
            try {
                var d = JSONArray(co_text)
                if(d.length() != 0 ) {
                    for (i in 0 until d.length()) {
                        var st = d[i].toString()
                        word += st
                        if (i != d.length() - 1) {
                            word += "\n"
                        }
                    }
                }else{
                    word = co_text!!
                }

            }catch (e:JSONException){
                word = co_text!!
            }
        }


        return word
    }
    fun Getcoinfo():ArrayList<String>{
        var result :ArrayList<String>  = arrayListOf()
        var word ='"'
        if (co_info?.isNullOrEmpty() == false) {

            var context = co_info
            try {
                var d = JSONArray(co_info)

                for (i in 0 until d.length()) {
                    var st = d[i].toString()
                    result.add(st)
                }
            }catch (e:JSONException){
                result.add(co_info!!)
            }
        }


        return result
    }
//    fun coTextList():ArrayList<String>{
//        var result :ArrayList<String>  = arrayListOf()
//        var word ='"'
//        if (co_text?.isNotEmpty() == true) {
//            Log.d("테스트1", co_text.toString())
//            var context = co_text
//            co_text
//            var d = JSONArray(co_text)
//            var min = 0
//            var max = 0
//            for ((count, i) in findIndexes(word.toString(),context.toString())!!.withIndex()){
//                if (solution(count)){
//                    min = i
//                }else{
//                    max = i
//                    var resultString = context?.substring(min+1,max)
//                    if (resultString != null) {
//                        result.add(resultString)
//                    }
//                }
//
//
//            }
//
//        }
//
//
//        return result
//    }
    fun solution(num: Int) = num % 2 == 0

    fun findIndexes(word: String, document: String): List<Int>? {
        val indexList: MutableList<Int> = ArrayList()
        var index = document.indexOf(word)
        while (index != -1) {
            indexList.add(index)
            index = document.indexOf(word, index + word.length)
        }
        return indexList
    }

}
