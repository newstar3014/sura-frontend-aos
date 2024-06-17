package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.MatchingMiniData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.data.ResultMatchingMiniData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MatchingViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("?")
    var my_seq = MutableLiveData<String>("")
    var type = MutableLiveData<String>("")
    var matchingdata = MutableLiveData<ArrayList<MatchingMiniData>>(arrayListOf())
    var page = MutableLiveData<Int>(1)
    var ppp = MutableLiveData<Int>(10)
    var swipe = MutableLiveData<Boolean>(false)
    var delindex =MutableLiveData<Int>()
    var cursorPosition = MutableLiveData<Int>(-1)
    var toast = MutableLiveData<String>("")
    fun GetMatchigmini() {

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java).GetMatchingMini(
                my_seq.value!!,
                type.value,
                page.value!!,
                ppp.value!!
            )
            d!!.enqueue(object : retrofit2.Callback<ResultMatchingMiniData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultMatchingMiniData>,
                    response: Response<ResultMatchingMiniData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows != null) {
                                if(rows.size!=0) {
                                    matchingdata.value =rows
                                    Log.d("매칭", matchingdata.value.toString())
                                }
                            }
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultMatchingMiniData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
    fun delete_request(pc_seq:String,target_seq:String,sub_seq:String,index:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(MatchingService::class.java).DeleteRequest(pc_seq,target_seq,sub_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            delindex.value = index
                            toast.value = "요청이 삭제되었습니다."
                        }else{
                            toast.value = "요청삭제에 실패하였습니다."
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
