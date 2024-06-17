package com.supercarlounge.supercar.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.round
import kotlin.math.roundToInt

data class LocationSpotData(

    var spot_seq: String?,
    var spot_area: String?,
    var spot_detail_area: String?,
    var spot_cate: String?,
    var spot_main_image: String?,
    var spot_sub_image: ArrayList<String>,
    var spot_title: String?,
    var spot_content: String?,
    var spot_detail: String?,
    var spot_address: String?,
    var spot_phone: String?,
    var spot_desc: ArrayList<LocationSpotDescData>,
    var spot_menu: ArrayList<LocationSpotDescData>,
    var spot_date: String?,
    var spot_lat: String?,
    var spot_lon: String?,
    var distance: String?,
    var bookmarkCheck: String?,
    var cate_name: String?,
    var bookCount: Int?,
    var spot_badge: String?,
    var badgeStartDate: String?,
    var badgeEndDate: String?,
    var badge_text: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(LocationSpotDescData::class.java.classLoader) as ArrayList<LocationSpotDescData>,
        parcel.readArrayList(LocationSpotDescData::class.java.classLoader) as ArrayList<LocationSpotDescData>,
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
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(spot_seq)
        parcel.writeString(spot_area)
        parcel.writeString(spot_detail_area)
        parcel.writeString(spot_cate)
        parcel.writeString(spot_main_image)
        parcel.writeList(spot_sub_image)
        parcel.writeString(spot_title)
        parcel.writeString(spot_content)
        parcel.writeString(spot_detail)
        parcel.writeString(spot_address)
        parcel.writeString(spot_phone)
        parcel.writeList(spot_desc)
        parcel.writeString(spot_date)
        parcel.writeString(spot_lat)
        parcel.writeString(spot_lon)
        parcel.writeString(distance)
        parcel.writeString(bookmarkCheck)
        parcel.writeString(cate_name)
        parcel.writeInt(bookCount!!)
        parcel.writeString(spot_badge)
        parcel.writeString(badgeStartDate)
        parcel.writeString(badgeEndDate)
        parcel.writeString(badge_text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationSpotData> {
        override fun createFromParcel(parcel: Parcel): LocationSpotData {
            return LocationSpotData(parcel)
        }

        override fun newArray(size: Int): Array<LocationSpotData?> {
            return arrayOfNulls(size)
        }
    }

    fun GetDistanceSet(): String {
        var result = "??m"
        if (distance.isNullOrEmpty() == false) {
            var d = distance!!.toDouble()
            result =String.format("%.1f", d)+"km"
        }
        return result
    }

    fun GetRegion(): String {
        var result = ""
        if (spot_detail_area.isNullOrEmpty()) {
            result = spot_area.toString()
        } else {
            result = spot_detail_area.toString()
        }
        return result
    }

    fun GetType(): String {
        var result = ""
        if (spot_desc.size != 0) {
            result = spot_desc[0].content.toString()
        }
        return result
    }
    fun GetCate(): String {
        var result = ""
        if (spot_desc.size != 0) {
            result = spot_desc[0].content.toString()
        }
        return result
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocationTag(): String {
        var result = ""
        var startDateTime: LocalDateTime? = null
        var endDateTime: LocalDateTime?= null
        if (badge_text?.isNotEmpty()==true){
            if (badgeStartDate?.isNotEmpty() == true) {
                var startDate = badgeStartDate?.split("-", " ", ":")
                if (startDate?.size!! > 4) {
                    startDateTime = LocalDateTime.of(
                        startDate[0].toInt(),
                        startDate[1].toInt(),
                        startDate[2].toInt(),
                        startDate[3].toInt(),
                        startDate[4].toInt()
                    )
                }

            }
            if (badgeEndDate?.isNotEmpty() == true) {
                var endDate = badgeEndDate?.split("-", " ", ":")
                if (endDate?.size!! > 4) {
                    endDateTime = LocalDateTime.of(
                        endDate[0].toInt(),
                        endDate[1].toInt(),
                        endDate[2].toInt(),
                        endDate[3].toInt(),
                        endDate[4].toInt()
                    )
                }

            }

            var currentDate = LocalDateTime.now()

            if (endDateTime != null && startDateTime != null){
                if (currentDate.isAfter(startDateTime) && currentDate.isBefore(endDateTime)) {
//                println("현재 시간이 시작 날짜와 끝 날짜 사이에 있습니다.")
                    result =badge_text.toString()
                } else if (currentDate.isEqual(startDateTime) || currentDate.isEqual(endDateTime)) {
                    result =badge_text.toString()
//                println("현재 시간이 시작 날짜 또는 끝 날짜와 같습니다.")
                } else {
                    result =""
                }

            }else{
                result =""
            }
        }







        return result
    }
}
