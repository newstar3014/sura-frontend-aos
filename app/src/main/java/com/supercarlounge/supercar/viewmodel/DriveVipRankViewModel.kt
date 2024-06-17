package com.supercarlounge.supercar.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.enumset.VipRankStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DriveVipRankViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("VIP DRIVE")
    var time = MutableLiveData<String>("")
    var heart = MutableLiveData<String>("")

    var checked1 = MutableLiveData<Boolean>(false)
    var vipranklist: MutableLiveData<ArrayList<DriveAwayData>> = MutableLiveData(arrayListOf())
    var isvipcheck = MutableLiveData<VipRankStatus>(VipRankStatus.DEFAULT)
    var data = MutableLiveData<DriveAwayData>()
    var toast = MutableLiveData<String>("")
    var isstart = MutableLiveData<Boolean>(false)
    var my_seq = MutableLiveData<String>("")
    var driveAuctionData = MutableLiveData<VipDriveAuctionData>()
    var isinputtype = MutableLiveData<Boolean>(false)
    var minHeartCount = MutableLiveData<Int>(0)
    var myHeartCount = MutableLiveData<Int>(0)
    var myReallyHeartCount = MutableLiveData<Int>(0)
    var vipDrvieFull = MutableLiveData<Int>(0)
    var myHeart= MutableLiveData<Int>()
    var sendsuccess = MutableLiveData<String>("")
    var isnotheart = MutableLiveData<Boolean>(false)
    fun getrank() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .getVipDrive(my_seq.value, "0", "0")
            d!!.enqueue(object : retrofit2.Callback<ResultDriveData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultDriveData>,
                    response: Response<ResultDriveData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var rows = data.drives

                        Log.d("getAlim : rows.size  ", rows.size.toString())
                        if (rows != null) {


                            vipranklist.value = rows
                            checkMinHeartCount(vipranklist.value!!)
                            if (  rows.size >= 10){
                                vipDrvieFull.value = vipDrvieFull.value?.plus(1)
                            }

//
                            Log.d("드라이브 VIP 전체 글", vipranklist.value.toString())
                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultDriveData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun checkMinHeartCount(list: ArrayList<DriveAwayData>) {

        if (list.size >0) {
            minHeartCount.value = list[list.size - 1].heart_count
        }
    }

    fun setnext() {

        if (heart.value.isNullOrEmpty() || heart.value.equals("0")) {
            toast.value = "구매가를 입력해주세요"
            return
        }
        if (!checked1.value!!) {

            toast.value = "약관에 동의해주세요."
            return
        }

        if (driveAuctionData.value != null) {
            if (minHeartCount.value!! >= myHeartCount.value!!) {
                isvipcheck.value = VipRankStatus.VIP_EXIST
            } else {

                isvipcheck.value = VipRankStatus.VIP_OVERLAP

            }
        } else {

            isvipcheck.value = VipRankStatus.VIP_NOT_EXIST

        }
        Log.d("vip체크", isvipcheck.value.toString())

    }
    @SuppressLint("SuspiciousIndentation")
    fun VipDriveUpload(b_seq:String){
        var json = JsonObject()
        json.addProperty("u_seq",my_seq.value)
        json.addProperty("heart_count",heart.value.toString())
        json.addProperty("b_seq",b_seq)

        var jsonsend = JsonObject()
        jsonsend.add("driveObj",json)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .VipDrivePost(jsonsend)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if(type.equals("success")){

                            sendsuccess.value = "vip"
                        }else if (type.equals("notheart")){
                            isnotheart.value = true
                        }else{

                            sendsuccess.value = "false"
                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }

//    fun GetMyHeart() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val d = NetworkManager.serveradapter.create(HeartService::class.java).GetMyheart(
//                my_seq.value!!
//            )
//            d!!.enqueue(object : retrofit2.Callback<ResultHeartCheckData> {
//                override fun onResponse(
//                    call: retrofit2.Call<ResultHeartCheckData>,
//                    response: Response<ResultHeartCheckData>
//                ) {
//                    var data = response.body()
//                    if (data != null) {
//                        var type = data.type
//                        if(type.equals("success")) {
//                            var heart =data.u_heart
//                            myReallyHeartCount.value= heart.toInt()
//                        }
//                    }
//
//                }
//
//                override fun onFailure(
//                    call: retrofit2.Call<ResultHeartCheckData>,
//                    t: Throwable
//                ) {
//                    Log.d("check", t.toString())
//                }
//
//            })
//
//        }
//    }
    fun checkvipdrive() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .GetMyVipDrive(my_seq.value)
            d!!.enqueue(object : retrofit2.Callback<ResultVipDriveAuctionData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultVipDriveAuctionData>,
                    response: Response<ResultVipDriveAuctionData>
                ) {
                    var data = response.body()
                    if (data != null) {

                        var rows = data.rows
                        if (rows != null) {
                            if (rows.size != 0) {
                                isinputtype.value = false
                                driveAuctionData.value = rows.get(0)
                                myHeartCount.value = driveAuctionData?.value?.heart_count
                                vipDrvieFull.value = vipDrvieFull.value?.plus(1)

                                Log.d("드라이브 VIP 본인 글", driveAuctionData.value.toString())
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultVipDriveAuctionData>,
                    t: Throwable
                ) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }

    fun RankUpVipDrive(da_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .RankUpVipDrive(my_seq.value!!, da_seq, heart.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {
                            toast.value = "VIP드라이브가 갱신되었습니다."
                        }else if(type.equals("notheart")){
                            isnotheart.value = true
                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }

    fun myHeartCheck(u_seq: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(UserService::class.java)
                .myheartCheck( u_seq, )
            d!!.enqueue(object : retrofit2.Callback<ResultMyheartCheckData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMyheartCheckData>,
                    response: Response<ResultMyheartCheckData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {
                            myHeart.value = data.u_heart
                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultMyheartCheckData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }


    fun CheckDrivePass() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java)
                .DrivePassCheck(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        var message = data.message
                        if (type.equals("success")) {

                        } else {

                        }


                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }

    }

}