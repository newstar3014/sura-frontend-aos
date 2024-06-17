package com.supercarlounge.supercar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.supercarlounge.supercar.Network.BannerService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.data.ConciergeData
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.data.ResultOneNotiData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ConciergeDetailViewModel : ViewModel() {
    var text = MutableLiveData<String>("")
    var titleText = MutableLiveData<String>("")
    var bottom_text= MutableLiveData<String>("이벤트 자세히 알아보기")
    var data :MutableLiveData<ConciergeData> = MutableLiveData()
    var islink = MutableLiveData<Boolean>(false)
    var isopen = MutableLiveData<Boolean>(false)
    var toast= MutableLiveData<String>("")
    var select_bannernoti_data = MutableLiveData<NotiData>()
    fun setlink(){
        islink.value = true
    }
    fun setopen(){
        isopen.value = true
    }
    fun getOneNoti(n_seq:String){


        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter!!.create(BannerService::class.java).getOneNoti(n_seq
            )
            d!!.enqueue(object : retrofit2.Callback<ResultOneNotiData> {
                override fun onResponse(
                    call: Call<ResultOneNotiData>,
                    response: Response<ResultOneNotiData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var data = data.rows
                        select_bannernoti_data.value = data

                    }

                }

                override fun onFailure(call: Call<ResultOneNotiData>, t: Throwable) {
                    Log.d("check", "failer")
                }

            })

        }
    }
}