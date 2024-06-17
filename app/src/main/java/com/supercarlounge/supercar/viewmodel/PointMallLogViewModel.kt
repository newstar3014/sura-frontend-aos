package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.HomerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.Network.PointMallService
import com.supercarlounge.supercar.Network.UserService
import com.supercarlounge.supercar.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.math.log

class PointMallLogViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("포인트 이용내역")
    var nowpoint = MutableLiveData<String>("0")
    var toast = MutableLiveData<String>("")
    var logdata = MutableLiveData<ArrayList<PointMallLogData>>(arrayListOf())
    var order = MutableLiveData<String>("전체")
    var ppp = MutableLiveData<Int>(10)
    var nowpage = MutableLiveData<Int>(1)
    var totalPage = MutableLiveData<Int>(0)
    var totalCount = MutableLiveData<Int>(0)
    var issend = MutableLiveData<Boolean>(false)
    var nowmonth = MutableLiveData<String>("5")
    var nowmonthpoint = MutableLiveData<String>("0")
    var isorderup = MutableLiveData<Boolean>(false)
    fun resetpage (){
        nowpage.value = 1
    }
    fun getpointlog(isreset:Boolean){
        issend.value = true
        var ordertext = order.value!!
        var orvalue = "0"
        if(ordertext.equals("전체")){
            orvalue = "0"
        }else if (ordertext.equals("적립")){
            orvalue = "1"
        }else if (ordertext.equals("차감")){
            orvalue = "2"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PointMallService::class.java).GetPointLogList(my_seq.value!!,orvalue,nowpage.value!!,ppp.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultPointMallLogData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPointMallLogData>,
                    response: Response<ResultPointMallLogData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var totalpage = data.totalPage
                            var totalcount = data.totalCount
                            totalPage.value = totalpage
                            totalCount.value = totalcount
                            var rows = data.rows
                            if (rows != null) {
                                if (isreset) {
                                    logdata.value = rows
                                }else{
                                    var data = logdata.value
                                    data!!.addAll(rows)
                                    logdata.value = data
                                }
                            }
                        }
                    }
                    issend.value = false
                }

                override fun onFailure(call: retrofit2.Call<ResultPointMallLogData>, t: Throwable) {
                    Log.d("failer", t.message!!)
                    issend.value = false
                }

            })

        }
    }
    fun GetMyPoint(){

        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(PointMallService::class.java).GetMyPoint(my_seq.value!!)
            d!!.enqueue(object : retrofit2.Callback<ResultPointData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultPointData>,
                    response: Response<ResultPointData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if(type.equals("success")) {
                            var rows = data.rows
                            if (rows.size != 0) {
                              var point = rows.get(0).u_point
                                var delpoint = rows.get(0).deletePoint
                                nowpoint.value = point
                                nowmonthpoint.value = delpoint
                            }
                        }
                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultPointData>, t: Throwable) {
                    Log.d("failer", t.message!!)

                }

            })

        }
    }
}
