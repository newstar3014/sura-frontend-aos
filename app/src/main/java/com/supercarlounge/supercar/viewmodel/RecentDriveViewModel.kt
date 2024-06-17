package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RecentDriveViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("최근 내 드라이브")
    var my_seq = MutableLiveData<String>("")
    var b_seq = MutableLiveData<String>("")
    var type = MutableLiveData<String>("")
    var recentDrivelist = MutableLiveData<ArrayList<RecentDriveListData>>(arrayListOf())
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(10)
    var checked_heart_pay = MutableLiveData<Boolean>(false)
    var swipe = MutableLiveData<Boolean>(false)
    var delindex =MutableLiveData<Int>()
    var cursorPosition = MutableLiveData<Int>(-1)
    var toast = MutableLiveData<String>("")
    var dialogset = MutableLiveData<Boolean>(false)
    var recentdata = MutableLiveData<RecentDriveData>()
    var bubbleRick = MutableLiveData<Boolean>(false)
    var passCheck = MutableLiveData<Boolean>(false)
    var select_anonymousNickname = MutableLiveData<String>("")
    var miniprofilestatus = MutableLiveData<Boolean>(false)
    var selectrecentlist = MutableLiveData<RecentDriveListData>()
    fun GetRecentList() {
        Log.d("나의seq", my_seq.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(DriveService::class.java).GetRecentList(
                my_seq.value!!,
                b_seq.value!!,
                type.value!!,
                page.value!!,
                ppp.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultRecentDriveListData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultRecentDriveListData>,
                    response: Response<ResultRecentDriveListData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {
                                if(rows.size!=0) {

                                    recentDrivelist.value =rows
                                    if ( recentDrivelist.value!= null) {
                                        Log.d(
                                            "내 최근 드라이브 열람 기록",
                                            recentDrivelist.value.toString()
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultRecentDriveListData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }


    fun GetRecentData() {
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(DriveService::class.java).getRecentData(
                b_seq.value!!,
            )
            d!!.enqueue(object : retrofit2.Callback<ResultRecentData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultRecentData>,
                    response: Response<ResultRecentData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            if (data.rows!= null){
                                if (data.rows.size != 0){
                                    recentdata.value = data.rows[0]
                                    var title = data.rows[0].b_title
                                    if (title.isNullOrEmpty() == false) {
                                        titleText.value = title!!
                                    }
                                }

                            }

                        }else if(type.equals("notexist")){
                            toast.value = "해당 게시글이 없습니다"
                        }else{

                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultRecentData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }

    fun HeartPaymentSet(c_gender:String,c_pay:String) {
        if (bubbleRick.value == false){
            bubbleRick.value = true

        var rdata = recentdata.value!!
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).HeartPaySet(
                rdata.b_seq!!.toString(),
                c_gender,
                c_pay
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            rdata.b_payment = c_pay
                            rdata.b_same_gender = c_gender
                            recentdata.value = rdata
                        }
                    }
                    bubbleRick.value = false
                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                    bubbleRick.value = false
                }

            })

        }
        }
    }

    fun myPageViewChk(mypage_type:String,use_seq:String) {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).myPageViewChk(
                mypage_type,
                use_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            GetRecentList()
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
    fun checkset(){
        var ncheck =checked_heart_pay.value!!
        if(ncheck){
            checked_heart_pay.value = false
        }else{
            checked_heart_pay.value = true
        }
    }
    fun ddialogset(){
        dialogset.value = true
    }
    fun GetMatchingData(u_seq:String,sub_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetMatchingData(u_seq, sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ProfileMatchingData> {
                override fun onResponse(
                    call: Call<ProfileMatchingData>,
                    response: Response<ProfileMatchingData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type != null) {
                            if (type.equals("success")) {

                                if (data != null) {
                                    var status = data.pc_status
                                    var pcok= data.pc_ok
                                    if (data.pc_status.equals("2")&&pcok.equals("Y")){
                                        miniprofilestatus.value = true
                                    }else{
                                        miniprofilestatus.value = false
                                    }
                                }
                            }
                        }else{
                            miniprofilestatus.value = false
                        }
                    }else{
                        miniprofilestatus.value = false
                    }
                }

                override fun onFailure(call: Call<ProfileMatchingData>, t: Throwable) {
                    Log.d("MatchingDataError", t.message!!)
                }
            })
        }
    }
}
