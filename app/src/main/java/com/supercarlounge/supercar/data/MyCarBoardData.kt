package com.supercarlounge.supercar.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.supercarlounge.supercar.enumset.TimeValue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class MyCarBoardData(
    val mmi_seq: Int,
    val mmi_title: String?,
    val mmi_contents: String?,
    val mmi_first: String?,
    val mmi_second: String?,
    val mmi_third: String?,
    val mrc_nickname: String?,
    val u_seq: Int,
    val u_gender: String?,
    val mmi_img: String?,
    val mmi_views: Int,
    val carb_seq: Int,
    val mmi_date: String?,
    val carb_name: String?,
    val carb_image: String?,
    val carm_seq: String?,
    val carm_name: String?,
    val csm_seq: String?,
    val csm_name: String?,
    val pc_ok: String?,
    val pc_status: Int,
    var likeCnt: Int,
    val comCnt: Int,
    var bookMarkCheck: String?,
    var likeCheck: String?,
    var rank_check: String?,
    var rank_num:Int,
    var rank_score:String?

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
       parcel.readString(),
        parcel.readString(),
                parcel.readString(),
                        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mmi_seq)
        parcel.writeString(mmi_title)
        parcel.writeString(mmi_contents)
        parcel.writeString(mmi_first)
        parcel.writeString(mmi_second)
        parcel.writeString(mmi_third)
        parcel.writeString(mrc_nickname)
        parcel.writeInt(u_seq)
        parcel.writeString(u_gender)
        parcel.writeString(mmi_img)
        parcel.writeInt(mmi_views)
        parcel.writeInt(carb_seq)
        parcel.writeString(mmi_date)
        parcel.writeString(carb_name)
        parcel.writeString(carb_image)
        parcel.writeString(carm_seq)
        parcel.writeString(carm_name)
        parcel.writeString(csm_seq)
        parcel.writeString(csm_name)
        parcel.writeString(pc_ok)
        parcel.writeInt(pc_status)
        parcel.writeInt(likeCnt)
        parcel.writeInt(comCnt)
        parcel.writeString(bookMarkCheck)
        parcel.writeString(likeCheck)
        parcel.writeString(rank_check)
        parcel.writeInt(rank_num)
        parcel.writeString(rank_score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyCarBoardData> {
        override fun createFromParcel(parcel: Parcel): MyCarBoardData {
            return MyCarBoardData(parcel)
        }

        override fun newArray(size: Int): Array<MyCarBoardData?> {
            return arrayOfNulls(size)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target," ") // '-'를 공백으로 바꿔준다.
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(result)

            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("변환된 시간", timeInMilliseconds.toString())
        return timeInMilliseconds
    }

    fun setTime(time : Long) :String{
        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        var diffTime = (curTime- time) / 1000
        var msg: String? = null
        if(diffTime < TimeValue.SEC.value ) {
            msg = "방금 전"
            return msg.toString()
        }else {
            for (i in TimeValue.values()) {
                diffTime = diffTime.div(i.value)


                if (diffTime > 14 && i == TimeValue.HOUR ||i > TimeValue.HOUR){
                    msg = "지난글"
                    return msg.toString()
                }
                if (diffTime < i.maximum) {
                    msg=i.msg
                    break
                }
            }
        }
        return diffTime.toString() + msg.toString()
    }
    fun getlike():String{
        var like = ""

        val thousands = likeCnt / 1000
//        val stack: LinkedList<Int> = LinkedList<Int>()
//        while(likeCnt > 0){
//            stack.push(likeCnt % 10);
//            likeCnt = likeCnt / 10;
//        }
//
//        if (likeCnt >= 10000){
//
//            likeCnt.
//
//            like ="+${likeCnt}"
//
//        }else{
            like ="+${likeCnt}"
//        }


        return like
    }
    fun getComents():String{
        var comment = ""

        val thousands = comCnt / 1000
//        val stack: LinkedList<Int> = LinkedList<Int>()
//        while(likeCnt > 0){
//            stack.push(likeCnt % 10);
//            likeCnt = likeCnt / 10;
//        }
//
//        if (likeCnt >= 10000){
//
//            likeCnt.
//
//            like ="+${likeCnt}"
//
//        }else{
        comment ="+${comCnt}"
//        }


        return comment
    }
    fun getlikeCount():String{
       var likeCnt = likeCnt.toString()

        return likeCnt
    }

    fun getViews():String{
        var views = ""

        val thousands = likeCnt / 1000
//        val stack: LinkedList<Int> = LinkedList<Int>()
//        while(likeCnt > 0){
//            stack.push(likeCnt % 10);
//            likeCnt = likeCnt / 10;
//        }
//
//        if (likeCnt >= 10000){
//
//            likeCnt.
//
//            like ="+${likeCnt}"
//
//        }else{
        views ="+${mmi_views}"
//        }


        return views
    }

    fun getTitle():String{
        var result = ""
        result = "$mmi_title"
        return  result
    }

//    fun getmessageTitle():String{
//        if(!b_title.isNullOrEmpty()) {
//            return decodeset(b_title!!)!!
//        }else{
//            return  ""
//        }
//
//    }
//
//    fun getmessageText():String{
//        if(!b_contents.isNullOrEmpty()) {
//            return decodeset(b_contents!!)!!
//        }else{
//            return  ""
//        }
//
//    }
//    fun decodeset(text: String?): String? {
//        val myString: String? = null
//        return try {
//
//            URLDecoder
//                .decode(
//                    text, "UTF-8"
//                )
//        } catch (e: UnsupportedEncodingException) {
//            text
//        }
//    }
}