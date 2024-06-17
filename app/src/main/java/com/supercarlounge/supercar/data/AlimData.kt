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

data class AlimData (var u_nickname:String,
                     var u_phone:String?,
             var pa_seq:Int,
                var pa_send_seq:Int,
                var pa_give_seq:Int,
                var pa_nick:String,
                var pa_body:String,
                var pa_date:String ,
                var pa_screen:String,
                var pa_data:String,
                var pa_check:Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(u_nickname)
        parcel.writeString(u_phone)
        parcel.writeInt(pa_seq)
        parcel.writeInt(pa_send_seq)
        parcel.writeInt(pa_give_seq)
        parcel.writeString(pa_nick)
        parcel.writeString(pa_body)
        parcel.writeString(pa_date)
        parcel.writeString(pa_screen)
        parcel.writeString(pa_data)
        parcel.writeInt(pa_check)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlimData> {
        override fun createFromParcel(parcel: Parcel): AlimData {
            return AlimData(parcel)
        }

        override fun newArray(size: Int): Array<AlimData?> {
            return arrayOfNulls(size)
        }
    }
    fun timestring():String{
       var longset = dateTimeToMillSec(pa_date)
        return setTime(longset)
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

    fun getTitle():String{
        var title = ""

        var myNick = u_nickname
        var givenNick = pa_nick




        var type1 = "BoardComment"
        var type2 = "BoardSubComment"

        var type3 = "MycarComment"
        var type4 = "MycarSubComment"

        var type5 = "SendProfile"
        var type6 = "ReadProfile"
        var type7 = "ApplyProfile"

        var type8 = "SendLike"
        var type9 = "GivenLike"
        var type10 = "AcceptLike"

        var type11 = "SendOpenPhone"
        var type12 = "OpenPhone"

        var type13 = "BoardLike"
        var type14 = "DriveLike"
        var type15 = "MycarLike"

        var type16 = "SuraMaster"
        var type17 = "Login"
        var type18 = "RejectScreen"


        var type19 = "SendDrive"
        var type20 = "GivenDrive"
        var type21 = "LocationDrive"

        var type22 = "DrivePass1Date"
        var type23 = "DrivePass3Date"
        var type24 = "DrivePass1Buy"
        var type25 = "DrivePass30Buy"

        var type26 = "HeartCharge"

        when(pa_screen){
            type1 ->{title = "작성한 게시글에 댓글이 달렸습니다! \uD83D\uDCAC"}
           type2 ->{title =  "작성한 댓글에 대댓글이 달렸습니다! \uD83D\uDCAC"}

            type3 ->{title = "작성한 게시글에 댓글이 달렸습니다! \uD83D\uDCAC"}
            type4 ->{title = "작성한 댓글에 대댓글이 달렸습니다! \uD83D\uDCAC"}

            type5 ->{title = "${givenNick}님에게 프로필 열람 신청을 했습니다. \uD83D\uDC8C"}
            type6 ->{title = "${givenNick}님에게 프로필 열람 승인 요청을 받았습니다. \uD83D\uDC8C"}
            type7 ->{title = "${givenNick}님의 프로필 열람을 승인했습니다. \uD83D\uDC8C"}

            type8 ->{title = "호감을 보냈습니다! \uD83D\uDC96"}
            type9 ->{title = "호감을 받았습니다!\uD83D\uDC96"}
            type10 ->{title = "호감을 수락했습니다! \uD83D\uDC96"}

            type11 ->{title = "${givenNick}님의 연락처를 공개했습니다. ☎"}
            type12 ->{title = "${givenNick}님이 연락처를 공개했습니다. ☎"}


            type13 ->{title = "작성하신 게시글에 좋아요를 받았습니다! \uD83D\uDC4D"}
            type14 ->{title = "작성하신 게시글에 좋아요를 받았습니다! \uD83D\uDC4D"}
            type15 ->{title = "작성하신 게시글에 좋아요를 받았습니다! \uD83D\uDC4D"}

            type16 ->{title = ""}
            type17 ->{title = ""}
            type18 ->{title = "슈라 프로필 변경 신청이 반려되었습니다."}

            type19 ->{title = "드라이브 신청을 완료했습니다. ⭐"}
            type20 ->{title = "드라이브 신청이 도착했습니다. ⭐"}
            type21 ->{title = "북마크하신 장소의 새 드라이브 글이 올라왔습니다. \uD83D\uDCCC"}

            type22 ->{title = "드라이브 패스권 만료가 1일 남았습니다! ✨"}
            type23 ->{title = "드라이브 패스권 만료가 3일 남았습니다! ✨"}
            type24 ->{title = "드라이브 패스권 1일 구매 완료했습니다! ✨"}
            type25 ->{title = "드라이브 패스권 30일 구매 완료했습니다! ✨"}
            type26 ->{title = "하트 충전을 완료했습니다! \uD83D\uDC97"}
        }



        return decodeset(title)!!

    }

    fun getBody():String{
        var type24 = "DrivePass1Buy"
        var type25 = "DrivePass30Buy"
        var result = ""
        when (pa_screen){
            type24, type25 ->{
                val target = "T"
                result =  pa_body.replace(target," ")
                Log.d("리스트", result)
            }
            else->{
                result = pa_body
            }


        }

        return decodeset(result)!!
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