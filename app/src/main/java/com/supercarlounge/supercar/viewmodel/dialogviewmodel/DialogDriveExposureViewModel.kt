package com.supercarlounge.supercar.viewmodel.dialogviewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ResultDriveAuctionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DialogDriveExposureViewModel : ViewModel() {

    var tv_ok = MutableLiveData<String>("")
    var tv_cancel = MutableLiveData<String>("")


    var tv_sub = MutableLiveData<String>("")
    var viewtype = MutableLiveData<Int>(0)
    var okText = MutableLiveData<String>("재등록")
    var minheart = MutableLiveData<String>("최소 필요 하트 196")
    var tv_time = MutableLiveData<String>("00:00:00")
    var end_date  = MutableLiveData<Long>(0)
    var isviptimer = MutableLiveData<Boolean>(false)
    var driveTimeText = MutableLiveData<String>("등록하기")
    var my_seq = MutableLiveData<String>("")
    var deficientCount =MutableLiveData<String>("")
    var addHeartCount =MutableLiveData<String>("")
    fun GetMyvipDrive() {
        Log.d("ㅇㅇ", my_seq.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(DriveService::class.java).GetMyVipData(
                my_seq.value!!,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultDriveAuctionData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDriveAuctionData>,
                    response: Response<ResultDriveAuctionData>
                ) {
                    Log.d("ㅇㅇ", "44")
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        Log.d("ㅇㅇ", type)
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {

                                var vip =rows
                                var dd =vip.end_date
                                Log.d("ㅇㅇ", vip.end_date)
                                end_date.value =dateTimeToMillSec(vip.end_date)
                                isviptimer.value =true

                            }
                        }else{


                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultDriveAuctionData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

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
    fun settime(){
        var endtime =end_date.value!!
        var nowtime = Date().time
        var dd =endtime-nowtime
        var sec = dd /1000;
        var min = sec / 60;
        var hour = min / 60;
        var m = min - (hour*60)
        var s = sec %60
        var lasttime = Date(dd)
        if(hour <=0 && m<=0 && s <0){
            driveTimeText.value = "00:00:00"
            isviptimer.value =false
        }else {
            var tt = hour.toString() + ":" + m + ":" + s+""
            driveTimeText.value = tt
        }

    }
}