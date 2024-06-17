package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.DriveService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.BannerData
import com.supercarlounge.supercar.data.ConciergeCategoryData
import com.supercarlounge.supercar.data.ResultVipLoungeData
import com.supercarlounge.supercar.data.ResultViptypeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class VipLoungeViewModel : ViewModel() {
    var my_seq = MutableLiveData<String>("")
    var text = MutableLiveData<String>("")
    var image = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var viewStack = MutableLiveData<Int>(1)
    var viplounge_gauge = MutableLiveData<Int>(0)
    var viplounge_Check = MutableLiveData<Int>(0)
    var ondial = MutableLiveData<Boolean>(false)
    var toast = MutableLiveData<String>("")
    var tablist= MutableLiveData<ArrayList<ConciergeCategoryData>>(arrayListOf())
    var bannertop= MutableLiveData<ArrayList<BannerData>>(arrayListOf())
    fun setdial(){
        ondial.value =true
    }
    fun stack(){
        if(viplounge_Check.value!!>=100) {
            viewStack.value = 2
        }else{
            toast.value ="아직 VIP 회원이 아닙니다"
        }
    }

    fun getVipGauge(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).GetVipGauge(
                my_seq.value!!

            )
            d!!.enqueue(object : retrofit2.Callback<ResultVipLoungeData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultVipLoungeData>,
                    response: Response<ResultVipLoungeData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type =data.type
                        if(type.equals("success")) {
                            var scoreset = data.score

                            if (scoreset != null) {

                                try {
                                    Log.d("브아이피게이지", scoreset.toInt().toString())
                                    viplounge_gauge.value = scoreset.toInt()
                                } catch (e: NumberFormatException) {
//                                    viplounge_gauge.value = 0
                                }
                            }

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultVipLoungeData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }

    fun CheckVipType(){
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(DriveService::class.java).CheckViptype(
                my_seq.value!!

            )

            d!!.enqueue(object : retrofit2.Callback<ResultViptypeData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultViptypeData>,
                    response: Response<ResultViptypeData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        Log.d("VIP_DATA", data.toString())
                        var type =data.type
                        if(type.equals("success")) {
                            var u_vip = data.u_vip
                            if (u_vip!= null) {
                                if (u_vip.equals("vip")) {
                                    viplounge_Check.value = 100
                                }else{
                                  getVipGauge()
                                }
                            }else{
                                getVipGauge()
                            }

                        }

                    }

                }

                override fun onFailure(call: retrofit2.Call<ResultViptypeData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
}