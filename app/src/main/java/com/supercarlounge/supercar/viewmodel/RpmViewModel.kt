package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultRpmData
import com.supercarlounge.supercar.enumset.RpmStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RpmViewModel : ViewModel() {
    var rpminputtype = MutableLiveData<Int>(0)
    var rpmindex = MutableLiveData<Int>(2)
    var rpm_prvindex = MutableLiveData<Int>(0)
    var my_seq = MutableLiveData<String>("")
    var sub_seq = MutableLiveData<String>("")
    var rpm_subtext = MutableLiveData<String>("RPM은 매칭 상대의 드라이브 매너,음악선곡센스,\n코스 선정 등을 평가하는 후기제도입니다.")
    var rpmstatus = MutableLiveData<RpmStatus>(RpmStatus.DEFAULT)
    var btn_text = MutableLiveData<String>("평가하기")
    var profile = MutableLiveData<String>()
    var setdialog = MutableLiveData<Boolean>()
    var toast =MutableLiveData<String>()
    var bubbleRick = MutableLiveData<Boolean>(false)
    var isnotheart = MutableLiveData<Boolean>(false)
    fun Setbtn(){
        setdialog.value = true
    }

    fun GetRpmAvg(u_seq:String){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetRpmScoreAvg(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultRpmData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultRpmData>,
                    response: Response<ResultRpmData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var score = data.score
                            if(score!=null) {
                                rpmindex.value = score.toInt()
                            }else{
                                rpmindex.value = 0
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultRpmData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
    fun InsertRpm(){
        if (bubbleRick.value ==false){
            bubbleRick.value = true

        var u_seq:Int = my_seq.value!!.toInt()
        var sub_seq:Int =     sub_seq.value!!.toInt()
            var json = JsonObject()
        json.addProperty("u_seq",sub_seq)
        json.addProperty("sub_seq",u_seq)
        json.addProperty("score",rpmindex.value)
        var sql = ""
        if(rpminputtype.value == 1) {
            sql = "sub_rpm_open = \"Y\""
        }else{
            sql = "u_rpm_open = \"Y\""
        }
        json.addProperty("updateStr",sql)
        var jsonsend = JsonObject()
        jsonsend.add("scoreObj",json)
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).InsertRpm(jsonsend)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type

                        if(type.equals("success")) {
                                rpmstatus.value = RpmStatus.RPM_COMPLETE
                        }else{
                            if (data.message.equals("RPM INSERT ERR")){
                                rpmstatus.value = RpmStatus.RPM_COMPLETE
                                toast.value = "RPM이 이미 등록 되었습니다"
                            }
                        }
                        Log.d("RPM_COMPLETE", rpmstatus.value.toString())
                    }
                    bubbleRick.value = false

                }

                override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    bubbleRick.value = false
                }

            })

        }
        }
    }

    fun ResetRpm(){
        if (bubbleRick.value == false) {
            bubbleRick.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val d = NetworkManager.serveradapter!!.create(MatchingService::class.java)
                    .ResetRpm(my_seq.value)
                d!!.enqueue(object : retrofit2.Callback<ResultData> {
                    override fun onResponse(
                        call: retrofit2.Call<ResultData>,
                        response: Response<ResultData>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            var type = data.type
                            Log.d("리셋Rpm", data.toString())
                            if (type.equals("success")) {
                                rpmindex.value = 0
                                toast.value = "리셋 되었습니다"
                                //  rpmstatus.value = RpmStatus.V
                            } else if (type.equals("notheart")) {
                                isnotheart.value = true
                            }else if (type.equals("notreset")) {
                                toast.value ="초기화 할 RPM 점수가 없습니다"
                            }

                        }
                        bubbleRick.value = false
                    }

                    override fun onFailure(call: retrofit2.Call<ResultData>, t: Throwable) {
                        Log.d("failer", t.message!!)
                        bubbleRick.value = false
                    }

                })

            }
        }
    }

    fun GetRpmScore(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).GetRpmScore(sub_seq.value,my_seq.value)
            d!!.enqueue(object : retrofit2.Callback<ResultRpmData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultRpmData>,
                    response: Response<ResultRpmData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("데이터", data.toString())
                        var type = data.type

                        if(type.equals("success")) {
                           var score =data.score
                            rpmindex.value = score.toInt()

                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultRpmData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                }

            })

        }
    }
}
